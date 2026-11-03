
undefined8 FUN_ffff000090751094(int param_1)

{
  uint uVar1;
  undefined *puVar2;
  long lVar3;
  
  FUN_ffff00009075496c();
  uVar1 = 0;
  if (param_1 != 0x2b) {
    uVar1 = (uint)(param_1 != 0x2d);
  }
  lVar3 = 1;
  if (param_1 != 0x2b) {
    lVar3 = 2;
  }
  puVar2 = &DAT_ffff000090877c50 + (ulong)uVar1 * 0xbd8;
  lVar3 = lVar3 - (ulong)uVar1;
  do {
    FUN_ffff00009077ee50(puVar2,0,0xbd8);
    puVar2 = puVar2 + 0xbd8;
    lVar3 = lVar3 + -1;
  } while (lVar3 != 0);
  return 0;
}


