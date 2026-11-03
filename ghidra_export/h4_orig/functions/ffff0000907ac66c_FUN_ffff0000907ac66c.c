// ffff0000907ac66c  size=1440  FUN_ffff0000907ac66c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907ac66c(int param_1,int param_2)

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
  
  iVar3 = FUN_ffff00009071c6e8();
  iVar4 = FUN_ffff0000907ad7fc();
  if (iVar4 != 0) {
    FUN_ffff00009077e11c(s_boot_stage___d__BOOT_STAGE_BL2_E_ffff0000907dd653,iVar3,1);
  }
  if ((param_2 != 1) || (iVar3 != 1)) {
    if (DAT_ffff00009090aff0 != '\x01') {
      return 0x10400c;
    }
    if (_DAT_ffff00009090aff4 == 0) {
      return 0;
    }
    uVar10 = 0;
    iVar3 = 0;
    piVar11 = (int *)PTR_DAT_ffff000090839a80;
    do {
      iVar4 = *piVar11;
      if (iVar4 != 0) {
        if ((param_1 != 0) || (iVar4 != 3)) {
          if (param_1 == 2) {
            if (iVar4 == 3) {
LAB_ffff0000907acaa8:
              if (((piVar11[1] != 0) && (piVar11[6] != 0xffffffff)) &&
                 ((uint)piVar11[4] < (uint)piVar11[6])) {
                iVar3 = FUN_ffff0000907ad7fc();
                if (iVar3 != 0) {
                  FUN_ffff00009077e11c
                            (s___s__group___d___d__>__d__ffff0000907fc8d0,&DAT_ffff0000907f228a,
                             *piVar11,piVar11[4],piVar11[6]);
                }
                iVar3 = FUN_ffff0000907acc0c(*piVar11,piVar11[6]);
                iVar4 = FUN_ffff0000907ad7fc();
                if (iVar3 == 0) {
                  if (iVar4 != 0) {
                    FUN_ffff00009077e11c
                              (s___s__group__d_update_done_ffff0000907f5932,&DAT_ffff0000907f228a,
                               *piVar11);
                  }
                  iVar3 = 0;
                }
                else if (iVar4 != 0) {
                  FUN_ffff00009077e11c
                            (s___s__update_fail__ret___0x_x_ffff0000907d8909,&DAT_ffff0000907f228a,
                             iVar3);
                }
                goto LAB_ffff0000907aca64;
              }
            }
          }
          else if (iVar4 != 4) goto LAB_ffff0000907acaa8;
        }
        iVar4 = FUN_ffff0000907ad7fc();
        if (iVar4 != 0) {
          FUN_ffff00009077e11c
                    (s___s__group__d_bypass_update_ver_ffff0000907da236,&DAT_ffff0000907f228a,
                     *piVar11);
        }
      }
LAB_ffff0000907aca64:
      uVar10 = uVar10 + 1;
      piVar11 = piVar11 + 7;
      if (_DAT_ffff00009090aff4 <= uVar10) {
        return iVar3;
      }
    } while( true );
  }
  local_64 = 0;
  local_68 = 0;
  iVar3 = FUN_ffff0000907abf2c();
  bVar2 = DAT_ffff000090837ea9;
  bVar1 = DAT_ffff000090837ea8;
  if (iVar3 == 0) {
    uVar6 = (uint)DAT_ffff000090837ea8;
    uVar7 = (uint)DAT_ffff000090837ea9;
    if ((DAT_ffff000090837ea8 < DAT_ffff000090837ea9) && (DAT_ffff000090837ea9 != 0xff)) {
      iVar3 = FUN_ffff0000907ad7fc();
      uVar6 = uVar7;
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver_upda_ffff000090800093,&DAT_ffff0000907f228a,0,
                   DAT_ffff000090837ea8,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad7fc();
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e06f9,&DAT_ffff0000907f228a,0,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837eab;
    bVar1 = DAT_ffff000090837eaa;
    uVar7 = (uint)DAT_ffff000090837eaa;
    uVar8 = (uint)DAT_ffff000090837eab;
    if (6 < uVar6) {
      uVar6 = 7;
    }
    if ((DAT_ffff000090837eaa < DAT_ffff000090837eab) && (DAT_ffff000090837eab != 0xff)) {
      iVar3 = FUN_ffff0000907ad7fc();
      uVar7 = uVar8;
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver_upda_ffff000090800093,&DAT_ffff0000907f228a,1,
                   DAT_ffff000090837eaa,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad7fc();
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e06f9,&DAT_ffff0000907f228a,1,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837ead;
    bVar1 = DAT_ffff000090837eac;
    uVar8 = (uint)DAT_ffff000090837eac;
    uVar9 = (uint)DAT_ffff000090837ead;
    if (6 < uVar7) {
      uVar7 = 7;
    }
    if ((DAT_ffff000090837eac < DAT_ffff000090837ead) && (DAT_ffff000090837ead != 0xff)) {
      iVar3 = FUN_ffff0000907ad7fc();
      uVar8 = uVar9;
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver_upda_ffff000090800093,&DAT_ffff0000907f228a,2,
                   DAT_ffff000090837eac,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad7fc();
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e06f9,&DAT_ffff0000907f228a,2,
                   bVar1);
      }
    }
    bVar2 = DAT_ffff000090837eaf;
    bVar1 = DAT_ffff000090837eae;
    uVar9 = (uint)DAT_ffff000090837eae;
    uVar12 = (uint)DAT_ffff000090837eaf;
    if (6 < uVar8) {
      uVar8 = 7;
    }
    if ((DAT_ffff000090837eae < DAT_ffff000090837eaf) && (DAT_ffff000090837eaf != 0xff)) {
      iVar3 = FUN_ffff0000907ad7fc();
      uVar9 = uVar12;
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver_upda_ffff000090800093,&DAT_ffff0000907f228a,3,
                   DAT_ffff000090837eae,bVar2);
      }
    }
    else {
      iVar3 = FUN_ffff0000907ad7fc();
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s___s__sub_group___d__arb_ver___d_n_ffff0000907e06f9,&DAT_ffff0000907f228a,3,
                   bVar1);
      }
    }
    if (6 < uVar9) {
      uVar9 = 7;
    }
    if (_DAT_ffff00009090afec == 0) {
      iVar3 = FUN_ffff0000907aba58();
      if (iVar3 == 0) {
        if (DAT_ffff00009090aff0 == '\x01') {
          uVar10 = (ulong)_DAT_ffff00009090aff4;
          if (_DAT_ffff00009090aff4 != 0) {
            piVar11 = (int *)(PTR_DAT_ffff000090839a80 + 8);
            do {
              if (piVar11[-2] == 4) {
                _DAT_ffff00009090afec = *piVar11;
                goto LAB_ffff0000907ac988;
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
      iVar4 = FUN_ffff0000907ad7fc();
      if (iVar4 != 0) {
        FUN_ffff00009077e11c
                  (s___s__Get_subgroup_index_failed__r_ffff0000907f3b19,&DAT_ffff0000907f228a,iVar3)
        ;
      }
    }
    else {
LAB_ffff0000907ac988:
      iVar4 = _DAT_ffff00009090afec;
      iVar5 = FUN_ffff0000907acf10(_DAT_ffff00009090afec,&local_64);
      iVar3 = 0x104004;
      if (iVar5 == 0) {
        uVar6 = ~(-1 << (ulong)(uVar6 & 0x1f)) | ~(-1 << (ulong)(uVar7 & 0x1f)) << 8 |
                ~(-1 << (ulong)(uVar8 & 0x1f)) << 0x10 | ~(-1 << (ulong)(uVar9 & 0x1f)) << 0x18;
        if (local_64 == uVar6) {
LAB_ffff0000907acb90:
          iVar3 = FUN_ffff0000907ad7fc();
          if (iVar3 != 0) {
            FUN_ffff00009077e11c
                      (s___s__update_misc_sub_group_ok__ffff0000907e2296,&DAT_ffff0000907f228a);
          }
          return 0;
        }
        iVar5 = FUN_ffff0000907acee4(iVar4);
        if (iVar5 == 0) {
          iVar4 = FUN_ffff0000907acf10(iVar4,&local_68);
          if (iVar4 != 0) goto LAB_ffff0000907ac6e0;
          if (uVar6 == local_68) goto LAB_ffff0000907acb90;
          iVar3 = FUN_ffff0000907ad7fc();
          if (iVar3 != 0) {
            FUN_ffff00009077e11c
                      (s___s__write_0x_x__read_0x_x_ffff0000907d065e,&DAT_ffff0000907f228a,uVar6,
                       local_68);
          }
        }
        iVar3 = 0x104005;
      }
    }
  }
LAB_ffff0000907ac6e0:
  iVar4 = FUN_ffff0000907ad7fc();
  if (iVar4 != 0) {
    FUN_ffff00009077e11c
              (s___s__update_misc_group_fail__ret_ffff0000907e3dc9,&DAT_ffff0000907f228a,iVar3);
  }
  return iVar3;
}


