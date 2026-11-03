// ffff000090704e7c  size=680  FUN_ffff000090704e7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff000090704e7c(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  undefined8 uVar4;
  uint local_14;
  
  _DAT_ffff00009083d0e0 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea40e);
  if (_DAT_ffff00009083d0e0 == 0) {
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0,&local_14);
  lVar1 = _DAT_ffff00009083d0e0;
  if (iVar2 != 0) {
    return iVar2;
  }
  if ((local_14 & 0xf0) != 0x70) {
    FUN_ffff00009077de54
              (s__s__d__vendor_id_0x_X_is_incorre_ffff0000907f5a0a,
               s_mt6379_check_devid_ffff0000907e3f6b,0x213);
    return -7;
  }
  local_14 = 0;
  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10e,&local_14);
  if (-1 < iVar2) {
    local_14 = local_14 & 0xffffff80 | 0x2d;
    iVar2 = FUN_ffff000090737284(lVar1,0x10e);
    lVar1 = _DAT_ffff00009083d0e0;
    if (iVar2 == 0) {
      local_14 = 0;
      iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x109,&local_14);
      if (-1 < iVar2) {
        local_14 = local_14 & 0xffffff00 | 0x10;
        iVar2 = FUN_ffff000090737284(lVar1,0x109);
        lVar1 = _DAT_ffff00009083d0e0;
        if (iVar2 == 0) {
          local_14 = 0;
          DAT_ffff000090816608 = 500;
          iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10a,&local_14);
          if (-1 < iVar2) {
            local_14 = local_14 & 0xffffff7f;
            iVar2 = FUN_ffff000090737284(lVar1,0x10a);
            lVar1 = _DAT_ffff00009083d0e0;
            if (iVar2 == 0) {
              local_14 = 0;
              iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x10b,&local_14);
              if (-1 < iVar2) {
                local_14 = local_14 & 0xffffff00 | 6;
                iVar2 = FUN_ffff000090737284(lVar1,0x10b);
                lVar1 = _DAT_ffff00009083d0e0;
                if (iVar2 == 0) {
                  local_14 = 0;
                  iVar2 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e0,0x117,&local_14);
                  if (-1 < iVar2) {
                    local_14 = local_14 & 0xffffffe0 | 2;
                    iVar2 = FUN_ffff000090737284(lVar1,0x117);
                    if (iVar2 == 0) {
                      iVar2 = FUN_ffff000090705298(500);
                      if (iVar2 == 0) {
                        return 0;
                      }
                      pcVar3 = s__s__d__Failed_to_set_ichg_ffff0000907d71e9;
                      uVar4 = 0x2cf;
                      goto LAB_ffff000090705080;
                    }
                  }
                  pcVar3 = s__s__d__Failed_to_set_iprec_ffff000090800138;
                  uVar4 = 0x2c9;
                  goto LAB_ffff000090705080;
                }
              }
              pcVar3 = s__s__d__Failed_to_set_mivr_ffff0000907fc8e9;
              uVar4 = 0x2c3;
              goto LAB_ffff000090705080;
            }
          }
          pcVar3 = s__s__d__Failed_to_disable_ilim_ffff0000907e71e5;
          uVar4 = 0x2bd;
          goto LAB_ffff000090705080;
        }
      }
      pcVar3 = s__s__d__Failed_to_set_aicr_ffff0000907f23bb;
      uVar4 = 0x2b7;
      goto LAB_ffff000090705080;
    }
  }
  pcVar3 = s__s__d__Failed_to_set_cv_ffff0000907fc8d0;
  uVar4 = 0x2b1;
LAB_ffff000090705080:
  FUN_ffff00009077de54(pcVar3,s_mt6379_chg_init_setting_ffff0000907d2403,uVar4);
  FUN_ffff00009077de54
            (s__s__d__Failed_to_init_setting_ffff0000907e71c6,
             s__mt6379_charger_init_ffff0000907f23a6,0x359);
  return iVar2;
}


