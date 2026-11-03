
int FUN_ffff0000b8014858
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
  
  FUN_ffff0000b800a01c();
  piVar5 = (int *)FUN_ffff0000b804bd14(0x40,0x200);
  if (piVar5 == (int *)0x0) {
    iVar2 = -0xc;
    *param_5 = s_LK_MEM_ALIGN_ERROR_ffff0000b808b616;
  }
  else {
    lVar6 = FUN_ffff0000b804a99c(s_boot_para_ffff0000b809b605);
    if (lVar6 == 0) {
      FUN_ffff0000b804c33c
                (s_Partition___s__is_not_exist__ffff0000b80942a6,s_boot_para_ffff0000b809b605);
      *param_5 = s_LK_PARTITION_NOT_EXIST_ffff0000b8099a17;
      thunk_FUN_ffff0000b8082558(piVar5);
      iVar2 = -0x13;
    }
    else {
      iVar2 = FUN_ffff0000b804aa30(lVar6,piVar5,0,0x200);
      if (iVar2 < 0x200) {
        FUN_ffff0000b804c33c(s_failed_to_read_hdr___d__ffff0000b808a853);
        iVar2 = -5;
      }
      else {
        lVar8 = 0;
        do {
          if (*(long *)(lVar6 + 0x20) <= lVar8) {
LAB_ffff0000b80149d8:
            iVar2 = -0x16;
            goto LAB_ffff0000b80149e0;
          }
          if (*piVar5 != 0x58881688) {
            FUN_ffff0000b804c33c(s_partition_doesn_t_contain_dconfi_ffff0000b808dc81);
            goto LAB_ffff0000b80149d8;
          }
          *(undefined1 *)((long)piVar5 + 0x27) = 0;
          iVar2 = FUN_ffff0000b804d510(piVar5 + 2,param_1,0x20);
          if (iVar2 == 0) {
            uVar1 = piVar5[1];
            uVar9 = (ulong)uVar1;
            if (param_3 < uVar9) {
              iVar4 = 0;
              iVar2 = -0x16;
              pcVar7 = s_LK_AUTH_BUFFER_SIZE_FAIL_ffff0000b8099a2e;
              goto LAB_ffff0000b80149e8;
            }
            if (uVar1 == 0) {
              FUN_ffff0000b804c33c(s___s__total_len___0_ffff0000b8093279,param_1);
              iVar4 = 0;
              iVar2 = 0;
              goto LAB_ffff0000b80149ec;
            }
            iVar3 = thunk_FUN_ffff0000b805b018(s_boot_para_ffff0000b809b605);
            if (iVar3 == 0) {
              *param_5 = s_LK_SEC_DISABLE_ffff0000b808dc72;
            }
            else {
              iVar2 = FUN_ffff0000b8052c3c
                                (s_boot_para_ffff0000b809b605,param_1,piVar5,0x200,lVar8,4);
              if (iVar2 != 0) {
                local_64 = 0;
                iVar4 = FUN_ffff0000b802c7fc(0,&local_64);
                if (iVar4 != 0) {
                  FUN_ffff0000b804c33c(s_Read_efuse_fail_in_dconfig__0x_x_ffff0000b8089a39,iVar4);
                    /* WARNING: Subroutine does not return */
                  FUN_ffff0000b804b830
                            (s_platform_mediatek_common_dconfig_ffff0000b8087eff,0x91,
                             &DAT_ffff0000b8086efb);
                }
                if ((local_64._1_1_ == -0x10) || (iVar2 != 0x101022)) {
                  FUN_ffff0000b804c33c
                            (s__s_cert_chain_vfy_fail___0x_x__ffff0000b808dc52,param_1,iVar2);
                  iVar4 = 0;
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_CERT_CHAIN_FAIL_ffff0000b80942c4;
                  goto LAB_ffff0000b80149e8;
                }
                FUN_ffff0000b8073140();
                FUN_ffff0000b804c33c
                          (s__s_socid_mismatch__ignore___0x_x_ffff0000b808703d,param_1,0x101022);
              }
              iVar2 = thunk_FUN_ffff0000b806b29c(piVar5,0x200);
              if (iVar2 != 0) {
                FUN_ffff0000b804c33c(s__s_header_auth_fail___0x_x__ffff0000b8087f2f,param_1,iVar2);
                iVar4 = 0;
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_HEADER_AUTH_FAIL_ffff0000b809f4c5;
                goto LAB_ffff0000b80149e8;
              }
            }
            iVar4 = FUN_ffff0000b804aa30(lVar6,param_2,lVar8 + 0x200,uVar9);
            if ((long)iVar4 < (long)uVar9) {
              FUN_ffff0000b804c33c
                        (s_failed_to_read_load_partition____ffff0000b80952e4,lVar8 + 0x200,iVar4);
              iVar2 = -5;
              if (iVar3 == 0) {
                pcVar7 = s_LK_SBOOT_OFF_READ_PARTITION_FAIL_ffff0000b808e9f4;
              }
              else {
                pcVar7 = s_LK_SBOOT_ON_READ_PARTITION_FAIL_ffff0000b808e9d4;
              }
            }
            else {
              if (iVar3 == 0) {
                iVar2 = 0;
                goto LAB_ffff0000b80149ec;
              }
              iVar2 = thunk_FUN_ffff0000b806b460(param_2,uVar1);
              if (iVar2 == 0) {
                iVar2 = thunk_FUN_ffff0000b806f17c(param_4,0x20);
                if (iVar2 == 0) {
                  iVar2 = 0;
                  pcVar7 = s_LK_SEC_ENABLE_AUTH_PASS_ffff0000b8091669;
                }
                else {
                  FUN_ffff0000b804c33c(s_No_cert_socid_found__0x_x__ffff0000b809328d,iVar2);
                  FUN_ffff0000b804d070(param_4,0,0x20);
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_GET_CERT_SOCID_FAIL_ffff0000b809164d;
                }
              }
              else {
                FUN_ffff0000b804c33c(s_image_auth_fail___0x_x__ffff0000b8099a47,iVar2);
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_IMG_AUTH_FAIL_ffff0000b809247d;
              }
            }
            goto LAB_ffff0000b80149e8;
          }
          lVar8 = lVar8 + (ulong)((piVar5[1] + piVar5[0x11]) - 1U & -piVar5[0x11]) + 0x200;
          iVar2 = FUN_ffff0000b804aa30(lVar6,piVar5,lVar8,0x200);
        } while (0x1ff < iVar2);
        FUN_ffff0000b804c33c(s_failed_to_read_mkimg_hdr___llu___ffff0000b8096f8b,lVar8);
        iVar2 = -5;
      }
LAB_ffff0000b80149e0:
      iVar4 = 0;
      pcVar7 = s_LK_NO_DCONFIG_IMG_ffff0000b8097e1f;
LAB_ffff0000b80149e8:
      *param_5 = pcVar7;
LAB_ffff0000b80149ec:
      FUN_ffff0000b804a920(lVar6);
      thunk_FUN_ffff0000b8082558(piVar5);
      if (-1 < iVar2) {
        return iVar4;
      }
    }
  }
  return iVar2;
}


