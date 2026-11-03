// ffff00009072195c  size=980  FUN_ffff00009072195c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072195c(void)

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
  if (DAT_ffff000090860690 == '\x01') {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x99,
               s_g_env_init_done____DCONFIG_VALID_ffff0000907df697);
  }
  DAT_ffff000090860690 = 1;
  DAT_ffff000090860694 = 0;
  _DAT_ffff000090860698 = FUN_ffff00009077d514(0x600);
  if (_DAT_ffff000090860698 == 0) {
    pcVar3 = s__s__d__malloc_failed_ffff0000907ede42;
    uVar4 = 0xa0;
  }
  else {
    FUN_ffff00009077ee50(_DAT_ffff000090860698,0,0x600);
    iVar2 = FUN_ffff00009072227c
                      (s_dconfig_ffff0000907cf41c,_DAT_ffff000090860698,0x600,&local_80,
                       &PTR_DAT_ffff0000908184b0);
    lVar5 = _DAT_ffff000090860698;
    if (iVar2 != 0x600) {
      FUN_ffff00009077e11c
                (s__s__d__load_dconfig_partition_fa_ffff0000907e5f02,
                 s_dconfig_initenv_ffff0000907da8a6,0xa7,iVar2);
      goto LAB_ffff000090721ab0;
    }
    _DAT_ffff0000908606a8 = (char *)(_DAT_ffff000090860698 + 0x208);
    FUN_ffff00009077eb20(0xffff0000908606a0,_DAT_ffff000090860698 + 0x200,8);
    FUN_ffff00009077eb20(0xffff0000908606b0,lVar5 + 0x5f4,8);
    iVar2 = FUN_ffff00009077f0ac(0xffff0000908606a0,s_ENV_v1_ffff0000907d41a2);
    if ((iVar2 == 0) &&
       (iVar2 = FUN_ffff00009077f0ac(0xffff0000908606b0,s_ENV_v1_ffff0000907d41a2),
       pcVar3 = _DAT_ffff0000908606a8, iVar2 == 0)) {
      lVar8 = 0;
      iVar2 = 0;
      iVar6 = 0;
      _DAT_ffff0000908606b8 = *(int *)(lVar5 + 0x5fc);
      do {
        pbVar1 = (byte *)(_DAT_ffff0000908606a8 + lVar8);
        lVar8 = lVar8 + 2;
        iVar2 = iVar2 + (uint)*pbVar1;
        iVar6 = iVar6 + (uint)pbVar1[1];
      } while (lVar8 != 0x3ec);
      if (iVar6 + iVar2 != _DAT_ffff0000908606b8) {
        FUN_ffff00009077e11c
                  (s__s__d__checksum_mismatch_s__d_d___ffff0000907d41a9,
                   s_dconfig_initenv_ffff0000907da8a6,0xb4);
        goto LAB_ffff000090721ab0;
      }
      FUN_ffff00009077e11c
                (s__s__d__print_env_data_ffff0000907fedc7,s_dconfig_printenv_ffff0000907da8b6,0x85);
      if (*_DAT_ffff0000908606a8 != '\0') {
        lVar5 = 0;
        uVar9 = 0;
        do {
          lVar8 = (long)(int)uVar9;
          uVar10 = uVar9;
          if ((int)uVar9 < 0x3ed) {
            uVar10 = 0x3ec;
          }
          pcVar7 = _DAT_ffff0000908606a8 + lVar8;
          uVar9 = uVar9 + 1;
          lVar8 = (ulong)uVar10 - lVar8;
          do {
            pcVar7 = pcVar7 + 1;
            if (lVar8 == 0) goto LAB_ffff000090721bd4;
            uVar9 = uVar9 + 1;
            lVar8 = lVar8 + -1;
          } while (*pcVar7 != '\0');
          FUN_ffff00009077e11c
                    (s__s__d___>__s_ffff0000907d5d22,s_dconfig_printenv_ffff0000907da8b6,0x8f,
                     pcVar3 + lVar5);
          lVar5 = (long)(int)uVar9;
        } while (_DAT_ffff0000908606a8[lVar5] != '\0');
      }
LAB_ffff000090721bd4:
      _DAT_ffff0000908606c0 = FUN_ffff00009077d514(0x20000);
      if (_DAT_ffff0000908606c0 == 0) {
        pcVar3 = s__s__d__malloc_failed_ffff0000907ede42;
        uVar4 = 0xd3;
        _DAT_ffff0000908606c0 = 0;
      }
      else {
        iVar2 = FUN_ffff00009072227c
                          (s_dconfig_dt_ffff0000907ec27a,_DAT_ffff0000908606c0,0x20000,&local_a0,
                           &PTR_DAT_ffff0000908184b0);
        if (iVar2 < 1) {
          FUN_ffff00009077e11c
                    (s__s__d__No_dconfig_dt_image_ffff0000907f44dc,
                     s_dconfig_initenv_ffff0000907da8a6,0xcd);
          thunk_FUN_ffff0000907c314c(_DAT_ffff0000908606c0);
          _DAT_ffff0000908606c0 = 0;
          _DAT_ffff0000908606c8 = 0;
        }
        else {
          _DAT_ffff0000908606c8 = iVar2;
          FUN_ffff00009077e11c
                    (s__s__d__dconfig_dtb_image_found___ffff0000907d7725,
                     s_dconfig_initenv_ffff0000907da8a6,0xc6,iVar2);
          _DAT_ffff0000908606c0 = FUN_ffff00009077d618(_DAT_ffff0000908606c0,_DAT_ffff0000908606c8);
          if (_DAT_ffff0000908606c0 == 0) {
            pcVar3 = s__s__d__dconfig_dtb_buffer_resize_ffff0000907f973d;
            uVar4 = 0xc9;
            goto LAB_ffff000090721a8c;
          }
        }
        iVar2 = FUN_ffff00009077eae8(&local_80,&local_a0,0x20);
        if (iVar2 == 0) {
          FUN_ffff00009077eb20(0xffff0000908606cc,&local_80,0x20);
          DAT_ffff000090860694 = 1;
          FUN_ffff00009077e11c
                    (s__s__d__dconfig_initialize_succes_ffff0000907e0f21,
                     s_dconfig_initenv_ffff0000907da8a6,0xde);
          return;
        }
        pcVar3 = s__s__d__cert_socid_mismatch_ffff00009080232b;
        uVar4 = 0xd8;
      }
    }
    else {
      pcVar3 = s__s__d__ENV_SIG_is_wrong_ffff0000907f44c3;
      uVar4 = 0xb8;
    }
  }
LAB_ffff000090721a8c:
  FUN_ffff00009077e11c(pcVar3,s_dconfig_initenv_ffff0000907da8a6,uVar4);
LAB_ffff000090721ab0:
  thunk_FUN_ffff0000907c314c(_DAT_ffff000090860698);
  _DAT_ffff0000908606c8 = 0;
  thunk_FUN_ffff0000907c314c(_DAT_ffff0000908606c0);
  _DAT_ffff000090860698 = 0;
  return;
}


