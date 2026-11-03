// ffff0000907ab798  size=540  FUN_ffff0000907ab798

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907ab798(void)

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
  if ((DAT_ffff00009090af30 & 1) == 0) {
    _DAT_ffff00009090af34 = FUN_ffff0000907987b4();
    puVar1 = PTR_DAT_ffff000090839a60;
    if (_DAT_ffff00009090af34 < 2) {
      iVar2 = 0x10400a;
    }
    else {
      uVar4 = 0;
      uVar6 = (ulong)_DAT_ffff00009090af34;
      piVar5 = (int *)(PTR_DAT_ffff000090839a60 + 4);
      do {
        if ((piVar5[-1] == 0) && (*piVar5 != 1)) {
          return 0x104007;
        }
        uVar4 = *piVar5 + uVar4;
        if (8 < uVar4) {
          iVar2 = FUN_ffff0000907ad53c();
          if (iVar2 == 0) {
            return 0x104007;
          }
          FUN_ffff00009077de54
                    (s___s__The_total_OTP_unit_cannot_b_ffff0000907e3caf,&DAT_ffff0000907f20e7,8);
          return 0x104007;
        }
        piVar5 = piVar5 + 7;
        uVar6 = uVar6 - 1;
      } while (uVar6 != 0);
      piVar5 = (int *)(PTR_DAT_ffff000090839a60 + 0xc);
      iVar2 = 1;
      uVar4 = _DAT_ffff00009090af34;
      if (_DAT_ffff00009090af34 < 2) {
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
      DAT_ffff00009090af30 = 1;
      do {
        if (puVar11[-3] == 0) {
          *puVar11 = 0;
        }
        else {
          iVar2 = FUN_ffff0000907ab9b4(puVar11[-4],&local_44);
          if (iVar2 != 0) {
            iVar3 = FUN_ffff0000907ad53c();
            if (iVar3 == 0) {
              return iVar2;
            }
            FUN_ffff00009077de54
                      (s___s__fail_to_get_otp_version_ffff0000907d3ad3,&DAT_ffff0000907f20e7);
            return iVar2;
          }
          *puVar11 = local_44;
          iVar2 = FUN_ffff0000907ad53c();
          if (iVar2 != 0) {
            FUN_ffff00009077de54
                      (s___s__group___d__start___d__end____ffff0000907e0612,&DAT_ffff0000907f20e7,
                       puVar11[-4],puVar11[-2],puVar11[-1],*puVar11);
          }
        }
        uVar6 = uVar6 + 1;
        puVar11 = puVar11 + 7;
      } while (uVar6 < _DAT_ffff00009090af34);
      iVar2 = 0;
    }
  }
  return iVar2;
}


