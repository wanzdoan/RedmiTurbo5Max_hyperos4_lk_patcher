
void FUN_ffff0000b8014020(void)

{
  byte *pbVar1;
  int iVar2;
  char *pcVar3;
  undefined8 uVar4;
  long lVar5;
  int iVar6;
  char *pcVar7;
  long lVar8;
  uint uVar9;
  uint uVar10;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  if (DAT_ffff0000b811af8c == '\x01') {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_dconfig_ffff0000b809f483,0x99,
               s_g_env_init_done____DCONFIG_VALID_ffff0000b808dc29);
  }
  DAT_ffff0000b811af8c = 1;
  DAT_ffff0000b811af90 = 0;
  DAT_ffff0000b811af98 = FUN_ffff0000b804bccc(0x600);
  if (DAT_ffff0000b811af98 == 0) {
    pcVar3 = s__s__d__malloc_failed_ffff0000b80960de;
    uVar4 = 0xa0;
  }
  else {
    FUN_ffff0000b804d070(DAT_ffff0000b811af98,0,0x600);
    iVar2 = FUN_ffff0000b8014858
                      (s_dconfig_ffff0000b8085167,DAT_ffff0000b811af98,0x600,&local_80,
                       &PTR_DAT_ffff0000b80ae030);
    lVar5 = DAT_ffff0000b811af98;
    if (iVar2 != 0x600) {
      FUN_ffff0000b804c33c
                (s__s__d__load_dconfig_partition_fa_ffff0000b809160f,
                 s_dconfig_initenv_ffff0000b808b5f5,0xa7,iVar2);
      goto LAB_ffff0000b8014174;
    }
    DAT_ffff0000b811afa8 = (char *)(DAT_ffff0000b811af98 + 0x208);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811afa0,DAT_ffff0000b811af98 + 0x200,8);
    FUN_ffff0000b804cd40(&DAT_ffff0000b811afb0,lVar5 + 0x5f4,8);
    iVar2 = FUN_ffff0000b804d248(&DAT_ffff0000b811afa0,s_ENV_v1_ffff0000b8087ed3);
    if ((iVar2 == 0) &&
       (iVar2 = FUN_ffff0000b804d248(&DAT_ffff0000b811afb0,s_ENV_v1_ffff0000b8087ed3),
       pcVar3 = DAT_ffff0000b811afa8, iVar2 == 0)) {
      lVar8 = 0;
      iVar2 = 0;
      iVar6 = 0;
      DAT_ffff0000b811afb8 = *(int *)(lVar5 + 0x5fc);
      do {
        pbVar1 = (byte *)(DAT_ffff0000b811afa8 + lVar8);
        lVar8 = lVar8 + 2;
        iVar2 = iVar2 + (uint)*pbVar1;
        iVar6 = iVar6 + (uint)pbVar1[1];
      } while (lVar8 != 0x3ec);
      if (iVar6 + iVar2 != DAT_ffff0000b811afb8) {
        FUN_ffff0000b804c33c
                  (s__s__d__checksum_mismatch_s__d_d___ffff0000b8087eda,
                   s_dconfig_initenv_ffff0000b808b5f5,0xb4);
        goto LAB_ffff0000b8014174;
      }
      FUN_ffff0000b804c33c
                (s__s__d__print_env_data_ffff0000b809f4ae,s_dconfig_printenv_ffff0000b808b605,0x85);
      if (*DAT_ffff0000b811afa8 != '\0') {
        lVar5 = 0;
        uVar9 = 0;
        do {
          lVar8 = (long)(int)uVar9;
          uVar10 = uVar9;
          if ((int)uVar9 < 0x3ed) {
            uVar10 = 0x3ec;
          }
          pcVar7 = DAT_ffff0000b811afa8 + lVar8;
          uVar9 = uVar9 + 1;
          lVar8 = (ulong)uVar10 - lVar8;
          do {
            pcVar7 = pcVar7 + 1;
            if (lVar8 == 0) goto LAB_ffff0000b8014298;
            uVar9 = uVar9 + 1;
            lVar8 = lVar8 + -1;
          } while (*pcVar7 != '\0');
          FUN_ffff0000b804c33c
                    (s__s__d___>__s_ffff0000b8088c90,s_dconfig_printenv_ffff0000b808b605,0x8f,
                     pcVar3 + lVar5);
          lVar5 = (long)(int)uVar9;
        } while (DAT_ffff0000b811afa8[lVar5] != '\0');
      }
LAB_ffff0000b8014298:
      DAT_ffff0000b811afc0 = FUN_ffff0000b804bccc(0x20000);
      if (DAT_ffff0000b811afc0 == 0) {
        pcVar3 = s__s__d__malloc_failed_ffff0000b80960de;
        uVar4 = 0xd3;
        DAT_ffff0000b811afc0 = 0;
      }
      else {
        iVar2 = FUN_ffff0000b8014858
                          (s_dconfig_dt_ffff0000b80952d9,DAT_ffff0000b811afc0,0x20000,&local_a0,
                           &PTR_DAT_ffff0000b80ae030);
        if (iVar2 < 1) {
          FUN_ffff0000b804c33c
                    (s__s__d__No_dconfig_dt_image_ffff0000b80999fb,
                     s_dconfig_initenv_ffff0000b808b5f5,0xcd);
          thunk_FUN_ffff0000b8082558(DAT_ffff0000b811afc0);
          DAT_ffff0000b811afc0 = 0;
          DAT_ffff0000b811afc8 = 0;
        }
        else {
          DAT_ffff0000b811afc8 = iVar2;
          FUN_ffff0000b804c33c
                    (s__s__d__dconfig_dtb_image_found___ffff0000b8089a02,
                     s_dconfig_initenv_ffff0000b808b5f5,0xc6,iVar2);
          DAT_ffff0000b811afc0 = FUN_ffff0000b804bdd0(DAT_ffff0000b811afc0,DAT_ffff0000b811afc8);
          if (DAT_ffff0000b811afc0 == 0) {
            pcVar3 = s__s__d__dconfig_dtb_buffer_resize_ffff0000b809c2d9;
            uVar4 = 0xc9;
            goto LAB_ffff0000b8014150;
          }
        }
        iVar2 = FUN_ffff0000b804cd08(&local_80,&local_a0,0x20);
        if (iVar2 == 0) {
          FUN_ffff0000b804cd40(&DAT_ffff0000b811afcc,&local_80,0x20);
          DAT_ffff0000b811af90 = 1;
          FUN_ffff0000b804c33c
                    (s__s__d__dconfig_initialize_succes_ffff0000b808e9b1,
                     s_dconfig_initenv_ffff0000b808b5f5,0xde);
          return;
        }
        pcVar3 = s__s__d__cert_socid_mismatch_ffff0000b80a0f02;
        uVar4 = 0xd8;
      }
    }
    else {
      pcVar3 = s__s__d__ENV_SIG_is_wrong_ffff0000b80999e2;
      uVar4 = 0xb8;
    }
  }
LAB_ffff0000b8014150:
  FUN_ffff0000b804c33c(pcVar3,s_dconfig_initenv_ffff0000b808b5f5,uVar4);
LAB_ffff0000b8014174:
  thunk_FUN_ffff0000b8082558(DAT_ffff0000b811af98);
  DAT_ffff0000b811afc8 = 0;
  thunk_FUN_ffff0000b8082558(DAT_ffff0000b811afc0);
  DAT_ffff0000b811af98 = 0;
  return;
}


