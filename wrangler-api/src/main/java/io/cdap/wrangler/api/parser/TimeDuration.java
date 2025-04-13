/*
 * Copyright © 2017-2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.Gson;

public class TimeDuration implements Token {
  private final String value;
  private final long nanoseconds;

  public TimeDuration(String value) {
    this.value = value;
    this.nanoseconds = parseTimeDuration(value);
  }

  @Override
  public Object value() {
    return value;
  }

  @Override
  public TokenType type() {
    return TokenType.TIME_DURATION;
  }

  @Override
  public JsonElement toJson() {
    return new Gson().toJsonTree(this);
  }

  public long getNanoseconds() {
    return nanoseconds;
  }

  private long parseTimeDuration(String input) {
    String normalized = input.toLowerCase();
    double number = Double.parseDouble(normalized.replaceAll("[^0-9.]", ""));
    String unit = normalized.replaceAll("[0-9.]", "");
    switch (unit) {
      case "ns": return (long) number;
      case "ms": return (long) (number * 1_000_000);
      case "s": return (long) (number * 1_000_000_000);
      case "m": return (long) (number * 60 * 1_000_000_000);
      case "h": return (long) (number * 3600 * 1_000_000_000);
      case "d": return (long) (number * 24 * 3600 * 1_000_000_000);
      default: throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
  }
}