
int FUN_ffff00009072227c
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
  
  FUN_ffff000090716918();
  piVar5 = (int *)FUN_ffff00009077d55c(0x40,0x200);
  if (piVar5 == (int *)0x0) {
    iVar2 = -0xc;
    *param_5 = s_LK_MEM_ALIGN_ERROR_ffff0000907da8c7;
  }
  else {
    lVar6 = FUN_ffff00009077c028(s_boot_para_ffff0000907f7bad);
    if (lVar6 == 0) {
      FUN_ffff00009077e11c
                (s_Partition___s__is_not_exist__ffff0000907eaa5a,s_boot_para_ffff0000907f7bad);
      *param_5 = s_LK_PARTITION_NOT_EXIST_ffff0000907f44f8;
      thunk_FUN_ffff0000907c314c(piVar5);
      iVar2 = -0x13;
    }
    else {
      iVar2 = FUN_ffff00009077c0bc(lVar6,piVar5,0,0x200);
      if (iVar2 < 0x200) {
        FUN_ffff00009077e11c(s_failed_to_read_hdr___d__ffff0000907d9012);
        iVar2 = -5;
      }
      else {
        lVar8 = 0;
        do {
          if (*(long *)(lVar6 + 0x20) <= lVar8) {
LAB_ffff0000907223fc:
            iVar2 = -0x16;
            goto LAB_ffff000090722404;
          }
          if (*piVar5 != 0x58881688) {
            FUN_ffff00009077e11c(s_partition_doesn_t_contain_dconfi_ffff0000907df6ef);
            goto LAB_ffff0000907223fc;
          }
          *(undefined1 *)((long)piVar5 + 0x27) = 0;
          iVar2 = FUN_ffff00009077f394(piVar5 + 2,param_1,0x20);
          if (iVar2 == 0) {
            uVar1 = piVar5[1];
            uVar9 = (ulong)uVar1;
            if (param_3 < uVar9) {
              iVar4 = 0;
              iVar2 = -0x16;
              pcVar7 = s_LK_AUTH_BUFFER_SIZE_FAIL_ffff0000907f450f;
              goto LAB_ffff00009072240c;
            }
            if (uVar1 == 0) {
              FUN_ffff00009077e11c(s___s__total_len___0_ffff0000907e90ff,param_1);
              iVar4 = 0;
              iVar2 = 0;
              goto LAB_ffff000090722410;
            }
            iVar3 = thunk_FUN_ffff000090798af8(s_boot_para_ffff0000907f7bad);
            if (iVar3 == 0) {
              *param_5 = s_LK_SEC_DISABLE_ffff0000907df6e0;
            }
            else {
              iVar2 = FUN_ffff000090788734
                                (s_boot_para_ffff0000907f7bad,param_1,piVar5,0x200,lVar8,4);
              if (iVar2 != 0) {
                local_64 = 0;
                iVar4 = FUN_ffff00009074a780(0,&local_64);
                if (iVar4 != 0) {
                  FUN_ffff00009077e11c(s_Read_efuse_fail_in_dconfig__0x_x_ffff0000907d775c,iVar4);
                    /* WARNING: Subroutine does not return */
                  FUN_ffff00009077cfcc
                            (s_platform_mediatek_common_dconfig_ffff0000907d41ce,0x91,
                             &DAT_ffff0000907d2506);
                }
                if ((local_64._1_1_ == -0x10) || (iVar2 != 0x101022)) {
                  FUN_ffff00009077e11c
                            (s__s_cert_chain_vfy_fail___0x_x__ffff0000907df6c0,param_1,iVar2);
                  iVar4 = 0;
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_CERT_CHAIN_FAIL_ffff0000907eaa78;
                  goto LAB_ffff00009072240c;
                }
                FUN_ffff0000907b7f78();
                FUN_ffff00009077e11c
                          (s__s_socid_mismatch__ignore___0x_x_ffff0000907d290e,param_1,0x101022);
              }
              iVar2 = thunk_FUN_ffff0000907aeaa4(piVar5,0x200);
              if (iVar2 != 0) {
                FUN_ffff00009077e11c(s__s_header_auth_fail___0x_x__ffff0000907d41fe,param_1,iVar2);
                iVar4 = 0;
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_HEADER_AUTH_FAIL_ffff0000907fedde;
                goto LAB_ffff00009072240c;
              }
            }
            iVar4 = FUN_ffff00009077c0bc(lVar6,param_2,lVar8 + 0x200,uVar9);
            if ((long)iVar4 < (long)uVar9) {
              FUN_ffff00009077e11c
                        (s_failed_to_read_load_partition____ffff0000907ec29d,lVar8 + 0x200,iVar4);
              iVar2 = -5;
              if (iVar3 == 0) {
                pcVar7 = s_LK_SBOOT_OFF_READ_PARTITION_FAIL_ffff0000907e0f64;
              }
              else {
                pcVar7 = s_LK_SBOOT_ON_READ_PARTITION_FAIL_ffff0000907e0f44;
              }
            }
            else {
              if (iVar3 == 0) {
                iVar2 = 0;
                goto LAB_ffff000090722410;
              }
              iVar2 = thunk_FUN_ffff0000907aec68(param_2,uVar1);
              if (iVar2 == 0) {
                iVar2 = thunk_FUN_ffff0000907b2984(param_4,0x20);
                if (iVar2 == 0) {
                  iVar2 = 0;
                  pcVar7 = s_LK_SEC_ENABLE_AUTH_PASS_ffff0000907e5f5c;
                }
                else {
                  FUN_ffff00009077e11c(s_No_cert_socid_found__0x_x__ffff0000907e9113,iVar2);
                  FUN_ffff00009077ee50(param_4,0,0x20);
                  iVar2 = -5;
                  pcVar7 = s_LK_AUTH_GET_CERT_SOCID_FAIL_ffff0000907e5f40;
                }
              }
              else {
                FUN_ffff00009077e11c(s_image_auth_fail___0x_x__ffff0000907f4528,iVar2);
                iVar2 = -5;
                pcVar7 = s_LK_AUTH_IMG_AUTH_FAIL_ffff0000907e7721;
              }
            }
            goto LAB_ffff00009072240c;
          }
          lVar8 = lVar8 + (ulong)((piVar5[1] + piVar5[0x11]) - 1U & -piVar5[0x11]) + 0x200;
          iVar2 = FUN_ffff00009077c0bc(lVar6,piVar5,lVar8,0x200);
        } while (0x1ff < iVar2);
        FUN_ffff00009077e11c(s_failed_to_read_mkimg_hdr___llu___ffff0000907ef62a,lVar8);
        iVar2 = -5;
      }
LAB_ffff000090722404:
      iVar4 = 0;
      pcVar7 = s_LK_NO_DCONFIG_IMG_ffff0000907f10a3;
LAB_ffff00009072240c:
      *param_5 = pcVar7;
LAB_ffff000090722410:
      FUN_ffff00009077bfac(lVar6);
      thunk_FUN_ffff0000907c314c(piVar5);
      if (-1 < iVar2) {
        return iVar4;
      }
    }
  }
  return iVar2;
}


