
int FUN_ffff0000be004938(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  undefined8 uVar4;
  uint local_14;
  
  DAT_ffff0000be0f9050 = FUN_ffff0000be02956c(s_mt6379_ffff0000be0b5f6e);
  if (DAT_ffff0000be0f9050 == 0) {
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0,&local_14);
  lVar1 = DAT_ffff0000be0f9050;
  if (iVar2 != 0) {
    return iVar2;
  }
  if ((local_14 & 0xf0) != 0x70) {
    FUN_ffff0000be05fd98
              (s__s__d__vendor_id_0x_X_is_incorre_ffff0000be0bde66,
               s_mt6379_check_devid_ffff0000be0b18f7,0x213);
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x10e,&local_14);
  if (-1 < iVar2) {
    local_14 = local_14 & 0xffffff80 | 0x2d;
    iVar2 = FUN_ffff0000be029920(lVar1,0x10e);
    lVar1 = DAT_ffff0000be0f9050;
    if (iVar2 == 0) {
      local_14 = 0;
      iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x109,&local_14);
      if (-1 < iVar2) {
        local_14 = local_14 & 0xffffff00 | 0x10;
        iVar2 = FUN_ffff0000be029920(lVar1,0x109);
        lVar1 = DAT_ffff0000be0f9050;
        if (iVar2 == 0) {
          local_14 = 0;
          DAT_ffff0000be0d5668 = 500;
          iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x10a,&local_14);
          if (-1 < iVar2) {
            local_14 = local_14 & 0xffffff7f;
            iVar2 = FUN_ffff0000be029920(lVar1,0x10a);
            lVar1 = DAT_ffff0000be0f9050;
            if (iVar2 == 0) {
              local_14 = 0;
              iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x10b,&local_14);
              if (-1 < iVar2) {
                local_14 = local_14 & 0xffffff00 | 6;
                iVar2 = FUN_ffff0000be029920(lVar1,0x10b);
                lVar1 = DAT_ffff0000be0f9050;
                if (iVar2 == 0) {
                  local_14 = 0;
                  iVar2 = FUN_ffff0000be029958(DAT_ffff0000be0f9050,0x117,&local_14);
                  if (-1 < iVar2) {
                    local_14 = local_14 & 0xffffffe0 | 2;
                    iVar2 = FUN_ffff0000be029920(lVar1,0x117);
                    if (iVar2 == 0) {
                      iVar2 = FUN_ffff0000be004d54(500);
                      if (iVar2 == 0) {
                        return 0;
                      }
                      pcVar3 = s__s__d__Failed_to_set_ichg_ffff0000be0a8bcc;
                      uVar4 = 0x2cf;
                      goto LAB_ffff0000be004b3c;
                    }
                  }
                  pcVar3 = s__s__d__Failed_to_set_iprec_ffff0000be0c4c29;
                  uVar4 = 0x2c9;
                  goto LAB_ffff0000be004b3c;
                }
              }
              pcVar3 = s__s__d__Failed_to_set_mivr_ffff0000be0c27de;
              uVar4 = 0x2c3;
              goto LAB_ffff0000be004b3c;
            }
          }
          pcVar3 = s__s__d__Failed_to_disable_ilim_ffff0000be0b3a9b;
          uVar4 = 0x2bd;
          goto LAB_ffff0000be004b3c;
        }
      }
      pcVar3 = s__s__d__Failed_to_set_aicr_ffff0000be0bba34;
      uVar4 = 0x2b7;
      goto LAB_ffff0000be004b3c;
    }
  }
  pcVar3 = s__s__d__Failed_to_set_cv_ffff0000be0c27c5;
  uVar4 = 0x2b1;
LAB_ffff0000be004b3c:
  FUN_ffff0000be05fd98(pcVar3,s_mt6379_chg_init_setting_ffff0000be0a54b7,uVar4);
  FUN_ffff0000be05fd98
            (s__s__d__Failed_to_init_setting_ffff0000be0b3a7c,
             s__mt6379_charger_init_ffff0000be0bba1f,0x359);
  return iVar2;
}


