
int FUN_ffff0000be01fef0
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
  
  FUN_ffff0000be019910();
  piVar5 = (int *)FUN_ffff0000be05f204(0x40,0x200);
  if (piVar5 == (int *)0x0) {
    iVar2 = -0xc;
    *param_5 = s_LK_MEM_ALIGN_ERROR_ffff0000be0ab4b0;
  }
  else {
    lVar6 = FUN_ffff0000be05df6c(s_boot_para_ffff0000be0bf529);
    if (lVar6 == 0) {
      FUN_ffff0000be05fd98
                (s_Partition___s__is_not_exist__ffff0000be0b63a1,s_boot_para_ffff0000be0bf529);
      *param_5 = s_LK_PARTITION_NOT_EXIST_ffff0000be0bcffb;
      thunk_FUN_ffff0000be09937c(piVar5);
      iVar2 = -0x13;
    }
    else {
      iVar2 = FUN_ffff0000be05e000(lVar6,piVar5,0,0x200);
      if (iVar2 < 0x200) {
        FUN_ffff0000be05fd98(s_failed_to_read_hdr___d__ffff0000be0aa2d1);
        iVar2 = -5;
      }
      else {
        lVar8 = 0;
        do {
          if (*(long *)(lVar6 + 0x20) <= lVar8) {
LAB_ffff0000be020070:
            iVar2 = -0x16;
            goto LAB_ffff0000be020078;
          }
          if (*piVar5 != 0x58881688) {
            FUN_ffff0000be05fd98(s_partition_doesn_t_contain_dconfi_ffff0000be0ae696);
            goto LAB_ffff0000be020070;
          }
          *(undefined1 *)((long)piVar5 + 0x27) = 0;
          iVar2 = FUN_ffff0000be060fa0(piVar5 + 2,param_1,0x20);
          if (iVar2 == 0) {
            uVar1 = piVar5[1];
            uVar9 = (ulong)uVar1;
            if (param_3 < uVar9) {
              iVar4 = 0;
              iVar2 = -0x16;
              pcVar7 = s_LK_AUTH_BUFFER_SIZE_FAIL_ffff0000be0bd012;
              goto LAB_ffff0000be020080;
            }
            if (uVar1 == 0) {
              FUN_ffff0000be05fd98(s___s__total_len___0_ffff0000be0b508f,param_1);
              iVar4 = 0;
              iVar2 = 0;
              goto LAB_ffff0000be020084;
            }
            iVar3 = thunk_FUN_ffff0000be075208(s_boot_para_ffff0000be0bf529);
            if (iVar3 == 0) {
              *param_5 = s_LK_SEC_DISABLE_ffff0000be0ae687;
            }
            else {
              iVar2 = FUN_ffff0000be067764
                                (s_boot_para_ffff0000be0bf529,param_1,piVar5,0x200,lVar8,4);
              if (iVar2 != 0) {
                local_64 = 0;
                iVar4 = FUN_ffff0000be035e94(0,&local_64);
                if (iVar4 != 0) {
                  FUN_ffff0000be05fd98(s_Read_efuse_fail_in_dconfig__0x_x_ffff0000be0a8f5d,iVar4);
                    /* WARNING: Subroutine does not return */
                  FUN_ffff0000be05ee98
                            (s_platform_mediatek_common_dconfig_ffff0000be0a6bf1,0x91,
                             &DAT_ffff0000be0a553d);
                }
                if ((local_64._1_1_ == -0x10) || (iVar2 != 0x101022)) {
                  FUN_ffff0000be05fd98
                            (s__s_cert_chain_vfy_fail___0x_x__ffff0000be0ae667,param_1,iVar2);
                  iVar4 = 0;
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_CERT_CHAIN_FAIL_ffff0000be0b63bf;
                  goto LAB_ffff0000be020080;
                }
                FUN_ffff0000be091d9c();
                FUN_ffff0000be05fd98
                          (s__s_socid_mismatch__ignore___0x_x_ffff0000be0a58ff,param_1,0x101022);
              }
              iVar2 = thunk_FUN_ffff0000be088c98(piVar5,0x200);
              if (iVar2 != 0) {
                FUN_ffff0000be05fd98(s__s_header_auth_fail___0x_x__ffff0000be0a6c21,param_1,iVar2);
                iVar4 = 0;
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_HEADER_AUTH_FAIL_ffff0000be0c3ef7;
                goto LAB_ffff0000be020080;
              }
            }
            iVar4 = FUN_ffff0000be05e000(lVar6,param_2,lVar8 + 0x200,uVar9);
            if ((long)iVar4 < (long)uVar9) {
              FUN_ffff0000be05fd98
                        (s_failed_to_read_load_partition____ffff0000be0b7618,lVar8 + 0x200,iVar4);
              iVar2 = -5;
              if (iVar3 == 0) {
                pcVar7 = s_LK_SBOOT_OFF_READ_PARTITION_FAIL_ffff0000be0af78c;
              }
              else {
                pcVar7 = s_LK_SBOOT_ON_READ_PARTITION_FAIL_ffff0000be0af76c;
              }
            }
            else {
              if (iVar3 == 0) {
                iVar2 = 0;
                goto LAB_ffff0000be020084;
              }
              iVar2 = thunk_FUN_ffff0000be088e5c(param_2,uVar1);
              if (iVar2 == 0) {
                iVar2 = thunk_FUN_ffff0000be08cb78(param_4,0x20);
                if (iVar2 == 0) {
                  iVar2 = 0;
                  pcVar7 = s_LK_SEC_ENABLE_AUTH_PASS_ffff0000be0b2dbd;
                }
                else {
                  FUN_ffff0000be05fd98(s_No_cert_socid_found__0x_x__ffff0000be0b50a3,iVar2);
                  FUN_ffff0000be060ae0(param_4,0,0x20);
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_GET_CERT_SOCID_FAIL_ffff0000be0b2da1;
                }
              }
              else {
                FUN_ffff0000be05fd98(s_image_auth_fail___0x_x__ffff0000be0bd02b,iVar2);
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_IMG_AUTH_FAIL_ffff0000be0b3fc4;
              }
            }
            goto LAB_ffff0000be020080;
          }
          lVar8 = lVar8 + (ulong)((piVar5[1] + piVar5[0x11]) - 1U & -piVar5[0x11]) + 0x200;
          iVar2 = FUN_ffff0000be05e000(lVar6,piVar5,lVar8,0x200);
        } while (0x1ff < iVar2);
        FUN_ffff0000be05fd98(s_failed_to_read_mkimg_hdr___llu___ffff0000be0b9ad5,lVar8);
        iVar2 = -5;
      }
LAB_ffff0000be020078:
      iVar4 = 0;
      pcVar7 = s_LK_NO_DCONFIG_IMG_ffff0000be0bad14;
LAB_ffff0000be020080:
      *param_5 = pcVar7;
LAB_ffff0000be020084:
      FUN_ffff0000be05def0(lVar6);
      thunk_FUN_ffff0000be09937c(piVar5);
      if (-1 < iVar2) {
        return iVar4;
      }
    }
  }
  return iVar2;
}


