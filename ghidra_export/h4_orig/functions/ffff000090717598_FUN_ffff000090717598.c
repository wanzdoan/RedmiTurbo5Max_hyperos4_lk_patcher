// ffff000090717598  size=140  FUN_ffff000090717598

undefined4 FUN_ffff000090717598(long param_1)

{
  char cVar1;
  undefined8 uVar2;
  ulong uVar3;
  undefined8 *puVar4;
  undefined4 uVar5;
  
  puVar4 = DAT_ffff000090839868;
  uVar2 = daif;
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 | 0x80;
  }
  cVar1 = *(char *)(param_1 + 4);
  *DAT_ffff000090839868 = 1;
  if (cVar1 == '\0') {
    uVar5 = FUN_ffff000090717e20(param_1 + 0x10);
    *puVar4 = 0;
  }
  else if ((*(byte *)(param_1 + 8) & 1) == 0) {
    uVar5 = 0;
    *puVar4 = 0;
  }
  else {
    uVar5 = 0;
    *(undefined1 *)(param_1 + 4) = 0;
    *puVar4 = 0;
  }
  if (((uint)uVar2 >> 7 & 1) == 0) {
    uVar3 = daif;
    daif = uVar3 & 0xffffffffffffff7f;
  }
  return uVar5;
}


