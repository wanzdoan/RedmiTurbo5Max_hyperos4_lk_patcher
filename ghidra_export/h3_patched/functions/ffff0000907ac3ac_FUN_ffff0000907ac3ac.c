// ffff0000907ac3ac  size=1440  FUN_ffff0000907ac3ac

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907ac3ac(int param_1,int param_2)

{
  byte bVar1;
  byte bVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  int *piVar11;
  uint uVar12;
  uint local_68;
  uint local_64;
  
  iVar3 = FUN_ffff00009071c55c();
  iVar4 = FUN_ffff0000907ad53c();
  if (iVar4 != 0) {
    FUN_ffff00009077de54(s_boot_stage___d__BOOT_STAGE_BL2_E_ffff0000907dd599,iVar3,1);
  }
  if ((param_2 != 1) || (iVar3 != 1)) {
    if (DAT_ffff00009090af30 != '\x01') {
      return 0x10400c;
    }
    if (_DAT_ffff00009090af34 == 0) {
      return 0;
    }
    uVar10 = 0;
    iVar3 = 0;
    piVar11 = (int *)PTR_DAT_ffff000090839a60;
    do {
      iVar4 = *piVar11;
      if (iVar4 != 0) {
        if ((param_1 != 0) || (iVar4 != 3)) {
          if (param_1 == 2) {
            if (iVar4 == 3) {
LAB_ffff0000907ac7e8:
              if (((piVar11[1] != 0) && (piVar11[6] != 0xffffffff)) &&
                 ((uint)piVar11[4] < (uint)piVar11[6])) {
                iVar3 = FUN_ffff0000907ad53c();
                if (iVar3 != 0) {
                  FUN_ffff00009077de54
                            (s___s__group___d___d__>__d__ffff0000907fc734,&DAT_ffff0000907f20e7,
                             *piVar11,piVar11[4],piVar11[6]);
                }
                iVar3 = FUN_ffff0000907ac94c(*piVar11,piVar11[6]);
                iVar4 = FUN_ffff0000907ad53c();
                if (iVar3 == 0) {
                  if (iVar4 != 0) {
                    FUN_ffff00009077de54
                              (s___s__group__d_update_done_ffff0000907f57ae,&DAT_ffff0000907f20e7,
                               *piVar11);
                  }
                  iVar3 = 0;
                }
                else if (iVar4 != 0) {
                  FUN_ffff00009077de54
                            (s___s__update_fail__ret___0x_x_ffff0000907d8873,&DAT_ffff0000907f20e7,
                             iVar3);
                }
                goto LAB_ffff0000907ac7a4;
              }
            }
          }
          else if (iVar4 != 4) goto LAB_ffff0000907ac7e8;
        }
        iVar4 = FUN_ffff0000907ad53c();
        if (iVar4 != 0) {
          FUN_ffff00009077de54
                    (s___s__group__d_bypass_update_ver_ffff0000907da188,&DAT_ffff0000907f20e7,
                     *piVar11);
        }
      }
LAB_ffff0000907ac7a4:
      uVar10 = uVar10 + 1;
      piVar11 = piVar11 + 7;
      if (_DAT_ffff00009090af34 <= uVar10) {
        return iVar3;
      }
    } while( true );
  }
  local_64 = 0;
  local_68 = 0;
  iVar3 = FUN_ffff0000907abc6c();
  bVar2 = DAT_ffff000090837e89;
  bVar1 = DAT_ffff000090837e88;
  if (iVar3 == 0) {
    uVar6 = (uint)DAT_ffff000090837e88;
    uVar7 = (uint)DAT_ffff000090837e89;
    if ((DAT_ffff000090837e88 < DAT_ffff000090837e89) && (DAT_ffff000090837e89 != 0xff)) {
      iVar3 = FUN_ffff0000907ad53c();
      uVar6 = uVar7;
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver_upda_ffff0000907ffeeb,&DAT_ffff0000907f20e7,0,
                   DAT_ffff000090837e88,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad53c();
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e063f,&DAT_ffff0000907f20e7,0,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837e8b;
    bVar1 = DAT_ffff000090837e8a;
    uVar7 = (uint)DAT_ffff000090837e8a;
    uVar8 = (uint)DAT_ffff000090837e8b;
    if (6 < uVar6) {
      uVar6 = 7;
    }
    if ((DAT_ffff000090837e8a < DAT_ffff000090837e8b) && (DAT_ffff000090837e8b != 0xff)) {
      iVar3 = FUN_ffff0000907ad53c();
      uVar7 = uVar8;
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver_upda_ffff0000907ffeeb,&DAT_ffff0000907f20e7,1,
                   DAT_ffff000090837e8a,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad53c();
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e063f,&DAT_ffff0000907f20e7,1,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837e8d;
    bVar1 = DAT_ffff000090837e8c;
    uVar8 = (uint)DAT_ffff000090837e8c;
    uVar9 = (uint)DAT_ffff000090837e8d;
    if (6 < uVar7) {
      uVar7 = 7;
    }
    if ((DAT_ffff000090837e8c < DAT_ffff000090837e8d) && (DAT_ffff000090837e8d != 0xff)) {
      iVar3 = FUN_ffff0000907ad53c();
      uVar8 = uVar9;
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver_upda_ffff0000907ffeeb,&DAT_ffff0000907f20e7,2,
                   DAT_ffff000090837e8c,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad53c();
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e063f,&DAT_ffff0000907f20e7,2,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837e8f;
    bVar1 = DAT_ffff000090837e8e;
    uVar9 = (uint)DAT_ffff000090837e8e;
    uVar12 = (uint)DAT_ffff000090837e8f;
    if (6 < uVar8) {
      uVar8 = 7;
    }
    if ((DAT_ffff000090837e8e < DAT_ffff000090837e8f) && (DAT_ffff000090837e8f != 0xff)) {
      iVar3 = FUN_ffff0000907ad53c();
      uVar9 = uVar12;
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver_upda_ffff0000907ffeeb,&DAT_ffff0000907f20e7,3,
                   DAT_ffff000090837e8e,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad53c();
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e063f,&DAT_ffff0000907f20e7,3,
                   bVar1);
      }
    }
    if (6 < uVar9) {
      uVar9 = 7;
    }
    if (_DAT_ffff00009090af2c == 0) {
      iVar3 = FUN_ffff0000907ab798();
      if (iVar3 == 0) {
        if (DAT_ffff00009090af30 == '\x01') {
          uVar10 = (ulong)_DAT_ffff00009090af34;
          if (_DAT_ffff00009090af34 != 0) {
            piVar11 = (int *)(PTR_DAT_ffff000090839a60 + 8);
            do {
              if (piVar11[-2] == 4) {
                _DAT_ffff00009090af2c = *piVar11;
                goto LAB_ffff0000907ac6c8;
              }
              piVar11 = piVar11 + 7;
              uVar10 = uVar10 - 1;
            } while (uVar10 != 0);
          }
          iVar3 = 0x104009;
        }
        else {
          iVar3 = 0x10400c;
        }
      }
      iVar4 = FUN_ffff0000907ad53c();
      if (iVar4 != 0) {
        FUN_ffff00009077de54
                  (s___s__Get_subgroup_index_failed__r_ffff0000907f3976,&DAT_ffff0000907f20e7,iVar3)
        ;
      }
    }
    else {
LAB_ffff0000907ac6c8:
      iVar4 = _DAT_ffff00009090af2c;
      iVar5 = FUN_ffff0000907acc50(_DAT_ffff00009090af2c,&local_64);
      iVar3 = 0x104004;
      if (iVar5 == 0) {
        uVar6 = ~(-1 << (ulong)(uVar6 & 0x1f)) | ~(-1 << (ulong)(uVar7 & 0x1f)) << 8 |
                ~(-1 << (ulong)(uVar8 & 0x1f)) << 0x10 | ~(-1 << (ulong)(uVar9 & 0x1f)) << 0x18;
        if (local_64 == uVar6) {
LAB_ffff0000907ac8d0:
          iVar3 = FUN_ffff0000907ad53c();
          if (iVar3 != 0) {
            FUN_ffff00009077de54
                      (s___s__update_misc_sub_group_ok__ffff0000907e21ae,&DAT_ffff0000907f20e7);
          }
          return 0;
        }
        iVar5 = FUN_ffff0000907acc24(iVar4);
        if (iVar5 == 0) {
          iVar4 = FUN_ffff0000907acc50(iVar4,&local_68);
          if (iVar4 != 0) goto LAB_ffff0000907ac420;
          if (uVar6 == local_68) goto LAB_ffff0000907ac8d0;
          iVar3 = FUN_ffff0000907ad53c();
          if (iVar3 != 0) {
            FUN_ffff00009077de54
                      (s___s__write_0x_x__read_0x_x_ffff0000907d063f,&DAT_ffff0000907f20e7,uVar6,
                       local_68);
          }
        }
        iVar3 = 0x104005;
      }
    }
  }
LAB_ffff0000907ac420:
  iVar4 = FUN_ffff0000907ad53c();
  if (iVar4 != 0) {
    FUN_ffff00009077de54
              (s___s__update_misc_group_fail__ret_ffff0000907e3ce1,&DAT_ffff0000907f20e7,iVar3);
  }
  return iVar3;
}


