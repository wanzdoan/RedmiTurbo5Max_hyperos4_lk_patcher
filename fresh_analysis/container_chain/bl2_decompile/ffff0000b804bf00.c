
ulong FUN_ffff0000b804bf00(char *param_1)

{
  char cVar1;
  byte bVar2;
  uint uVar3;
  byte *pbVar4;
  int iVar5;
  uint uVar6;
  ulong uVar7;
  char *pcVar8;
  byte *pbVar9;
  
  cVar1 = *param_1;
  if ((cVar1 == '0') && (param_1[1] == 'x')) {
    if (param_1[2] == '\0') {
      uVar7 = 0;
    }
    else {
      uVar7 = 0;
      pcVar8 = param_1 + 3;
      do {
        iVar5 = FUN_ffff0000b804c1fc();
        if (iVar5 == 0) {
          return uVar7;
        }
        bVar2 = pcVar8[-1];
        uVar3 = bVar2 - 0x30;
        if (9 < uVar3) {
          uVar6 = (uint)bVar2;
          uVar3 = uVar6 - 0x37;
          if (5 < bVar2 - 0x41) {
            uVar3 = 0;
          }
          if (uVar6 - 0x61 < 6) {
            uVar3 = uVar6 - 0x57;
          }
        }
        cVar1 = *pcVar8;
        uVar7 = uVar7 << 4 | (ulong)uVar3;
        pcVar8 = pcVar8 + 1;
      } while (cVar1 != '\0');
    }
  }
  else {
    uVar7 = 0;
    pcVar8 = param_1;
    if (cVar1 == '-') {
      pcVar8 = param_1 + 1;
    }
    if (*pcVar8 != '\0') {
      pbVar4 = (byte *)(param_1 + (cVar1 == '-'));
      do {
        pbVar9 = pbVar4 + 1;
        iVar5 = FUN_ffff0000b804c1e4();
        if (iVar5 == 0) break;
        uVar7 = ((ulong)*pbVar4 + uVar7 * 10) - 0x30;
        pbVar4 = pbVar9;
      } while (*pbVar9 != 0);
    }
    if (cVar1 == '-') {
      uVar7 = -uVar7;
    }
  }
  return uVar7;
}


