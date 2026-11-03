// ffff00009072203c  size=1044  FUN_ffff00009072203c

int FUN_ffff00009072203c
              (undefined8 param_1,undefined8 param_2,ulong param_3,undefined8 param_4,
              undefined8 *param_5)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int *piVar5;
  long lVar6;
  char *pcVar7;
  long lVar8;
  ulong uVar9;
  undefined4 local_64;
  
  FUN_ffff0000907167d4();
  piVar5 = (int *)FUN_ffff00009077d294(0x40,0x200);
  if (piVar5 == (int *)0x0) {
    iVar2 = -0xc;
    *param_5 = s_LK_MEM_ALIGN_ERROR_ffff0000907da7bc;
  }
  else {
    lVar6 = FUN_ffff00009077bd60(s_boot_para_ffff0000907f7a23);
    if (lVar6 == 0) {
      FUN_ffff00009077de54
                (s_Partition___s__is_not_exist__ffff0000907ea8d7,s_boot_para_ffff0000907f7a23);
      *param_5 = s_LK_PARTITION_NOT_EXIST_ffff0000907f438b;
      thunk_FUN_ffff0000907c2ad8(piVar5);
      iVar2 = -0x13;
    }
    else {
      iVar2 = FUN_ffff00009077bdf4(lVar6,piVar5,0,0x200);
      if (iVar2 < 0x200) {
        FUN_ffff00009077de54(s_failed_to_read_hdr___d__ffff0000907d8f7b);
        iVar2 = -5;
      }
      else {
        lVar8 = 0;
        do {
          if (*(long *)(lVar6 + 0x20) <= lVar8) {
LAB_ffff0000907221bc:
            iVar2 = -0x16;
            goto LAB_ffff0000907221c4;
          }
          if (*piVar5 != 0x58881688) {
            FUN_ffff00009077de54(s_partition_doesn_t_contain_dconfi_ffff0000907df64c);
            goto LAB_ffff0000907221bc;
          }
          *(undefined1 *)((long)piVar5 + 0x27) = 0;
          iVar2 = FUN_ffff00009077f0cc(piVar5 + 2,param_1,0x20);
          if (iVar2 == 0) {
            uVar1 = piVar5[1];
            uVar9 = (ulong)uVar1;
            if (param_3 < uVar9) {
              iVar4 = 0;
              iVar2 = -0x16;
              pcVar7 = s_LK_AUTH_BUFFER_SIZE_FAIL_ffff0000907f43a2;
              goto LAB_ffff0000907221cc;
            }
            if (uVar1 == 0) {
              FUN_ffff00009077de54(s___s__total_len___0_ffff0000907e8fbb,param_1);
              iVar4 = 0;
              iVar2 = 0;
              goto LAB_ffff0000907221d0;
            }
            iVar3 = thunk_FUN_ffff000090798838(s_boot_para_ffff0000907f7a23);
            if (iVar3 == 0) {
              *param_5 = s_LK_SEC_DISABLE_ffff0000907df63d;
            }
            else {
              iVar2 = FUN_ffff000090788474
                                (s_boot_para_ffff0000907f7a23,param_1,piVar5,0x200,lVar8,4);
              if (iVar2 != 0) {
                local_64 = 0;
                iVar4 = FUN_ffff00009074a4e8(0,&local_64);
                if (iVar4 != 0) {
                  FUN_ffff00009077de54(s_Read_efuse_fail_in_dconfig__0x_x_ffff0000907d76c6,iVar4);
                    /* WARNING: Subroutine does not return */
                  FUN_ffff00009077cd04
                            (s_platform_mediatek_common_dconfig_ffff0000907d4191,0x91,
                             &DAT_ffff0000907d24c9);
                }
                if ((local_64._1_1_ == -0x10) || (iVar2 != 0x101022)) {
                  FUN_ffff00009077de54
                            (s__s_cert_chain_vfy_fail___0x_x__ffff0000907df61d,param_1,iVar2);
                  iVar4 = 0;
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_CERT_CHAIN_FAIL_ffff0000907ea8f5;
                  goto LAB_ffff0000907221cc;
                }
                FUN_ffff00009077de54
                          (s__s_socid_mismatch__ignore___0x_x_ffff0000907d28d1,param_1,0x101022);
              }
              iVar2 = thunk_FUN_ffff0000907ae7dc(piVar5,0x200);
              if (iVar2 != 0) {
                FUN_ffff00009077de54(s__s_header_auth_fail___0x_x__ffff0000907d41c1,param_1,iVar2);
                iVar4 = 0;
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_HEADER_AUTH_FAIL_ffff0000907fec36;
                goto LAB_ffff0000907221cc;
              }
            }
            iVar4 = FUN_ffff00009077bdf4(lVar6,param_2,lVar8 + 0x200,uVar9);
            if ((long)iVar4 < (long)uVar9) {
              FUN_ffff00009077de54
                        (s_failed_to_read_load_partition____ffff0000907ec11a,lVar8 + 0x200,iVar4);
              iVar2 = -5;
              if (iVar3 == 0) {
                pcVar7 = s_LK_SBOOT_OFF_READ_PARTITION_FAIL_ffff0000907e0e93;
              }
              else {
                pcVar7 = s_LK_SBOOT_ON_READ_PARTITION_FAIL_ffff0000907e0e73;
              }
            }
            else {
              if (iVar3 == 0) {
                iVar2 = 0;
                goto LAB_ffff0000907221d0;
              }
              iVar2 = thunk_FUN_ffff0000907ae9a0(param_2,uVar1);
              if (iVar2 == 0) {
                iVar2 = thunk_FUN_ffff0000907b2314(param_4,0x20);
                if (iVar2 == 0) {
                  iVar2 = 0;
                  pcVar7 = s_LK_SEC_ENABLE_AUTH_PASS_ffff0000907e5e48;
                }
                else {
                  FUN_ffff00009077de54(s_No_cert_socid_found__0x_x__ffff0000907e8fcf,iVar2);
                  FUN_ffff00009077eb88(param_4,0,0x20);
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_GET_CERT_SOCID_FAIL_ffff0000907e5e2c;
                }
              }
              else {
                FUN_ffff00009077de54(s_image_auth_fail___0x_x__ffff0000907f43bb,iVar2);
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_IMG_AUTH_FAIL_ffff0000907e75dd;
              }
            }
            goto LAB_ffff0000907221cc;
          }
          lVar8 = lVar8 + (ulong)((piVar5[1] + piVar5[0x11]) - 1U & -piVar5[0x11]) + 0x200;
          iVar2 = FUN_ffff00009077bdf4(lVar6,piVar5,lVar8,0x200);
        } while (0x1ff < iVar2);
        FUN_ffff00009077de54(s_failed_to_read_mkimg_hdr___llu___ffff0000907ef4b9,lVar8);
        iVar2 = -5;
      }
LAB_ffff0000907221c4:
      iVar4 = 0;
      pcVar7 = s_LK_NO_DCONFIG_IMG_ffff0000907f0f32;
LAB_ffff0000907221cc:
      *param_5 = pcVar7;
LAB_ffff0000907221d0:
      FUN_ffff00009077bce4(lVar6);
      thunk_FUN_ffff0000907c2ad8(piVar5);
      if (-1 < iVar2) {
        return iVar4;
      }
    }
  }
  return iVar2;
}


