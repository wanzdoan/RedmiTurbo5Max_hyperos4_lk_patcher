// ffff00009077d850  size=232  FUN_ffff00009077d850

ulong FUN_ffff00009077d850(byte *param_1)

{
  byte bVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  byte *pbVar6;
  
  if (*param_1 == 0) {
    uVar5 = 0;
  }
  else if ((*param_1 == 0x30) && (param_1[1] == 0x78)) {
    if (param_1[2] == 0) {
      uVar5 = 0;
    }
    else {
      uVar5 = 0;
      pbVar6 = param_1 + 3;
      do {
        iVar3 = FUN_ffff00009077db60();
        if (iVar3 == 0) {
          return uVar5;
        }
        bVar1 = pbVar6[-1];
        uVar2 = bVar1 - 0x30;
        if (9 < uVar2) {
          uVar4 = (uint)bVar1;
          uVar2 = uVar4 - 0x37;
          if (5 < bVar1 - 0x41) {
            uVar2 = 0;
          }
          if (uVar4 - 0x61 < 6) {
            uVar2 = uVar4 - 0x57;
          }
        }
        bVar1 = *pbVar6;
        uVar5 = uVar5 << 4 | (ulong)uVar2;
        pbVar6 = pbVar6 + 1;
      } while (bVar1 != 0);
    }
  }
  else {
    uVar5 = 0;
    do {
      pbVar6 = param_1 + 1;
      iVar3 = FUN_ffff00009077db2c();
      if (iVar3 == 0) {
        return uVar5;
      }
      uVar5 = ((ulong)*param_1 + uVar5 * 10) - 0x30;
      param_1 = pbVar6;
    } while (*pbVar6 != 0);
  }
  return uVar5;
}


