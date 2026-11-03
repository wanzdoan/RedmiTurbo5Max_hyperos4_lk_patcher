
void FUN_ffff0000b804ba54(undefined1 *param_1,ulong param_2,long param_3)

{
  undefined1 uVar1;
  char *pcVar2;
  ulong uVar3;
  undefined1 uVar4;
  undefined1 *puVar5;
  bool bVar6;
  int iVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  long lVar12;
  
  pcVar2 = s_0x_016llx__ffff0000b80911a4;
  if (param_3 + param_2 >> 0x20 == 0) {
    pcVar2 = s_0x_08llx__ffff0000b809a1da;
  }
  if (param_2 != 0) {
    uVar11 = 0;
    uVar10 = param_2;
    do {
      uVar3 = uVar10;
      if (0xf < uVar10) {
        uVar3 = 0x10;
      }
      lVar12 = uVar3 - 0x10;
      FUN_ffff0000b804c33c(pcVar2,uVar11 + param_3);
      uVar8 = param_2 - uVar11;
      if (0xf < uVar8) {
        uVar8 = 0x10;
      }
      if (uVar8 == 0) {
LAB_ffff0000b804bb5c:
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b8086a00);
          bVar6 = lVar12 != -1;
          lVar12 = lVar12 + 1;
        } while (bVar6);
      }
      else {
        uVar9 = 0;
        do {
          FUN_ffff0000b804c33c(s__02hhx_ffff0000b809be59,param_1[uVar9]);
          uVar9 = uVar9 + 1;
        } while (uVar3 != uVar9);
        if (uVar10 < 0x10) goto LAB_ffff0000b804bb5c;
      }
      FUN_ffff0000b804c33c(&DAT_ffff0000b809cead);
      puVar5 = param_1;
      while (uVar8 != 0) {
        uVar4 = *puVar5;
        iVar7 = FUN_ffff0000b804c220(uVar4);
        uVar1 = 0x2e;
        if (iVar7 != 0) {
          uVar1 = uVar4;
        }
        FUN_ffff0000b804c33c(&DAT_ffff0000b808fa33,uVar1);
        uVar3 = uVar3 - 1;
        puVar5 = puVar5 + 1;
        uVar8 = uVar3;
      }
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
      param_1 = param_1 + 0x10;
      uVar11 = uVar11 + 0x10;
      uVar10 = uVar10 - 0x10;
    } while (uVar11 < param_2);
  }
  return;
}


