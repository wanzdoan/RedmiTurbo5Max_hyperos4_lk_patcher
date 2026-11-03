
undefined8 FUN_ffff0000b8032f04(int param_1)

{
  uint uVar1;
  undefined *puVar2;
  long lVar3;
  
  FUN_ffff0000b80367dc();
  uVar1 = 0;
  if (param_1 != 0x2b) {
    uVar1 = (uint)(param_1 != 0x2d);
  }
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  puVar2 = &DAT_ffff0000b8125590 + (ulong)uVar1 * 0xbd8;
  lVar3 = lVar3 - (ulong)uVar1;
  do {
    FUN_ffff0000b804d070(puVar2,0,0xbd8);
    puVar2 = puVar2 + 0xbd8;
    lVar3 = lVar3 + -1;
  } while (lVar3 != 0);
  return 0;
}


