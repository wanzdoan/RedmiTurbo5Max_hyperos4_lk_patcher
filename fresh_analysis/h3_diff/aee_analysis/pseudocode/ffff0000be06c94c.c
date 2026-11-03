
void FUN_ffff0000be06c94c(undefined8 param_1)

{
  byte bVar1;
  byte bVar2;
  byte bVar3;
  byte local_34 [4];
  
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x381,local_34,1);
  bVar3 = local_34[0];
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x382,local_34,1);
  bVar1 = local_34[0];
  if (local_34[0] != 0 || bVar3 != 0) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xfe | bVar3 >> 6 & 1;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xf7 | bVar3 >> 2 & 8;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xfb | bVar3 >> 2 & 4;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd13,local_34,1);
    bVar3 = bVar3 << 3;
    local_34[0] = local_34[0] & 0xbf | bVar3 & 0x40;
    FUN_ffff0000be029990(param_1,0xd13,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd13,local_34,1);
    local_34[0] = local_34[0] & 0xdf | bVar3 & 0x20;
    FUN_ffff0000be029990(param_1,0xd13,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd13,local_34,1);
    local_34[0] = local_34[0] & 0xef | bVar3 & 0x10;
    FUN_ffff0000be029990(param_1,0xd13,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd13,local_34,1);
    local_34[0] = local_34[0] & 0xf7 | bVar3 & 8;
    FUN_ffff0000be029990(param_1,0xd13,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd21,local_34,1);
    local_34[0] = (bVar1 & 0x10) << 3 | local_34[0] & 0x7f;
    FUN_ffff0000be029990(param_1,0xd21,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    bVar1 = bVar1 << 4;
    local_34[0] = bVar1 & 0x80 | local_34[0] & 0x7f;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xbf | bVar1 & 0x40;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xdf | bVar1 & 0x20;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd22,local_34,1);
    local_34[0] = local_34[0] & 0xef | bVar1 & 0x10;
    FUN_ffff0000be029990(param_1,0xd22,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0xd89,local_34,1);
  local_34[0] = local_34[0] | 0x40;
  FUN_ffff0000be029990(param_1,0xd89,local_34,1);
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x323,local_34,1);
  bVar1 = local_34[0] & 0x3f;
  if ((local_34[0] & 0x3f) != 0) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd11,local_34,1);
    local_34[0] = local_34[0] & 0xc0 | bVar1;
    FUN_ffff0000be029990(param_1,0xd11,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x324,local_34,1);
  bVar1 = local_34[0] & 0xf;
  if ((local_34[0] & 0xf) != 0) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd1a,local_34,1);
    local_34[0] = local_34[0] & 0xf0 | bVar1;
    FUN_ffff0000be029990(param_1,0xd1a,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x325,local_34,1);
  bVar1 = local_34[0];
  if (7 < local_34[0]) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd29,local_34,1);
    local_34[0] = local_34[0] & 0xe0 | bVar1 >> 3;
    FUN_ffff0000be029990(param_1,0xd29,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x326,local_34,1);
  bVar1 = local_34[0];
  if (7 < local_34[0]) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd2a,local_34,1);
    local_34[0] = local_34[0] & 0xe0 | bVar1 >> 3;
    FUN_ffff0000be029990(param_1,0xd2a,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x324,local_34,1);
  bVar1 = local_34[0];
  if (0xf < local_34[0]) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd28,local_34,1);
    local_34[0] = bVar1 & 0xf0 | local_34[0] & 0xf;
    FUN_ffff0000be029990(param_1,0xd28,local_34,1);
  }
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x327,local_34,1);
  bVar3 = local_34[0];
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x325,local_34,1);
  bVar2 = local_34[0];
  local_34[0] = 0;
  FUN_ffff0000be0299c8(param_1,0x326,local_34,1);
  bVar1 = local_34[0] & 7;
  if (((bVar2 & 7) != 0 || (bVar3 & 0x7f) != 0) || (local_34[0] & 7) != 0) {
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd30,local_34,1);
    local_34[0] = local_34[0] & 0x80 | bVar3 & 0x7f;
    FUN_ffff0000be029990(param_1,0xd30,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd24,local_34,1);
    local_34[0] = local_34[0] & 0xf8 | bVar2 & 7;
    FUN_ffff0000be029990(param_1,0xd24,local_34,1);
    local_34[0] = 0;
    FUN_ffff0000be0299c8(param_1,0xd24,local_34,1);
    local_34[0] = local_34[0] & 0x8f | bVar1 << 4;
    FUN_ffff0000be029990(param_1,0xd24,local_34,1);
  }
  return;
}


