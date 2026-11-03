
int FUN_ffff0000907aba58(void)

{
  undefined *puVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  int *piVar5;
  ulong uVar6;
  int iVar7;
  int iVar8;
  int iVar9;
  int iVar10;
  undefined4 *puVar11;
  undefined4 local_44;
  
  iVar2 = 0;
  local_44 = 0;
  if ((DAT_ffff00009090aff0 & 1) == 0) {
    DAT_ffff00009090aff4 = FUN_ffff000090798a74();
    puVar1 = PTR_DAT_ffff000090839a80;
    if (DAT_ffff00009090aff4 < 2) {
      iVar2 = 0x10400a;
    }
    else {
      uVar4 = 0;
      uVar6 = (ulong)DAT_ffff00009090aff4;
      piVar5 = (int *)(PTR_DAT_ffff000090839a80 + 4);
      do {
        if ((piVar5[-1] == 0) && (*piVar5 != 1)) {
          return 0x104007;
        }
        uVar4 = *piVar5 + uVar4;
        if (8 < uVar4) {
          iVar2 = FUN_ffff0000907ad7fc();
          if (iVar2 == 0) {
            return 0x104007;
          }
          FUN_ffff00009077e11c
                    (s___s__The_total_OTP_unit_cannot_b_ffff0000907e3d97,&DAT_ffff0000907f228a,8);
          return 0x104007;
        }
        piVar5 = piVar5 + 7;
        uVar6 = uVar6 - 1;
      } while (uVar6 != 0);
      piVar5 = (int *)(PTR_DAT_ffff000090839a80 + 0xc);
      iVar2 = 1;
      uVar4 = DAT_ffff00009090aff4;
      if (DAT_ffff00009090aff4 < 2) {
        uVar4 = 1;
      }
      uVar6 = (ulong)uVar4;
      do {
        iVar3 = piVar5[-2];
        if (iVar3 == 0) {
          iVar10 = -1;
          iVar8 = -1;
          iVar9 = -1;
          piVar5[-1] = -1;
          piVar5[0] = -1;
          iVar7 = 0;
        }
        else if (piVar5[-3] == 0) {
          iVar9 = 0;
          iVar7 = -1;
          iVar10 = 0x1f;
          piVar5[-1] = 0;
          piVar5[0] = 0;
          iVar8 = 0;
        }
        else {
          piVar5[-1] = iVar2;
          iVar9 = iVar3 + iVar2 + -1;
          iVar10 = iVar3 * 0x20 + -1;
          iVar7 = -1;
          *piVar5 = iVar9;
          iVar8 = iVar2;
          iVar2 = iVar3 + iVar2;
        }
        piVar5[1] = -1;
        piVar5[2] = iVar10;
        piVar5[3] = iVar7;
        if ((iVar9 - iVar8) - 8U < 0xfffffff7) {
          return 0x104009;
        }
        uVar6 = uVar6 - 1;
        piVar5 = piVar5 + 7;
      } while (uVar6 != 0);
      uVar6 = 0;
      puVar11 = (undefined4 *)(puVar1 + 0x10);
      DAT_ffff00009090aff0 = 1;
      do {
        if (puVar11[-3] == 0) {
          *puVar11 = 0;
        }
        else {
          iVar2 = FUN_ffff0000907abc74(puVar11[-4],&local_44);
          if (iVar2 != 0) {
            iVar3 = FUN_ffff0000907ad7fc();
            if (iVar3 == 0) {
              return iVar2;
            }
            FUN_ffff00009077e11c
                      (s___s__fail_to_get_otp_version_ffff0000907d3b10,&DAT_ffff0000907f228a);
            return iVar2;
          }
          *puVar11 = local_44;
          iVar2 = FUN_ffff0000907ad7fc();
          if (iVar2 != 0) {
            FUN_ffff00009077e11c
                      (s___s__group___d__start___d__end____ffff0000907e06cc,&DAT_ffff0000907f228a,
                       puVar11[-4],puVar11[-2],puVar11[-1],*puVar11);
          }
        }
        uVar6 = uVar6 + 1;
        puVar11 = puVar11 + 7;
      } while (uVar6 < DAT_ffff00009090aff4);
      iVar2 = 0;
    }
  }
  return iVar2;
}


