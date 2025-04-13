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

public class ByteSize implements Token {
  private final String value;
  private final long bytes;

  public ByteSize(String value) {
    this.value = value;
    this.bytes = parseByteSize(value);
  }

  @Override
  public Object value() {
    return value;
  }

  @Override
  public TokenType type() {
    return TokenType.BYTE_SIZE;
  }

  @Override
  public JsonElement toJson() {
    return new Gson().toJsonTree(this);
  }

  public long getBytes() {
    return bytes;
  }

  private long parseByteSize(String input) {
    String normalized = input.toLowerCase();
    double number = Double.parseDouble(normalized.replaceAll("[^0-9.]", ""));
    String unit = normalized.replaceAll("[0-9.]", "");
    switch (unit) {
      case "b": return (long) number;
      case "kb": return (long) (number * 1024);
      case "mb": return (long) (number * 1024 * 1024);
      case "gb": return (long) (number * 1024 * 1024 * 1024);
      case "tb": return (long) (number * 1024 * 1024 * 1024 * 1024);
      default: throw new IllegalArgumentException("Unknown byte unit: " + unit);
    }
  }
}