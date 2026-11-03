// ffff00009070611c  size=964  FUN_ffff00009070611c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009070611c(int *param_1,undefined4 *param_2,uint param_3)

{
  undefined4 uVar1;
  undefined8 uVar2;
  int iVar3;
  ulong uVar4;
  long lVar5;
  ulong uVar6;
  ushort local_70 [2];
  uint local_6c;
  uint local_68;
  uint local_64;
  
  uVar2 = _DAT_ffff00009083d088;
  local_70[0] = 0;
  if (1 < param_3) {
    FUN_ffff00009077e11c
              (s__s__d__bat_idx__u__BUG_ffff0000907ebdcb,s_mt6379_imp_conv_ffff0000907f3dfd,0x6d,
               param_3);
    return -7;
  }
  local_6c = 0;
  uVar1 = *(undefined4 *)(&DAT_ffff000090803db4 + (ulong)param_3 * 0x18);
  iVar3 = FUN_ffff000090737554(_DAT_ffff00009083d088,uVar1,&local_6c);
  if (-1 < iVar3) {
    local_6c = local_6c & 0xfffffff3;
    iVar3 = FUN_ffff00009073751c(uVar2,uVar1);
    uVar2 = _DAT_ffff00009083d088;
    if (iVar3 != 0) {
      return iVar3;
    }
    local_68 = 0;
    iVar3 = FUN_ffff000090737554(_DAT_ffff00009083d088,uVar1,&local_68);
    if (-1 < iVar3) {
      local_68 = local_68 & 0xfffffffc;
      iVar3 = FUN_ffff00009073751c(uVar2,uVar1);
      if (iVar3 != 0) {
        return iVar3;
      }
      uVar6 = (ulong)param_3;
      uVar1 = *(undefined4 *)(&DAT_ffff000090803db0 + uVar6 * 0x18);
      iVar3 = FUN_ffff00009073751c(_DAT_ffff00009083d088,uVar1,0);
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_clear_BAT_d_AUX_ffff0000907d8b6f,
                   s_mt6379_imp_conv_ffff0000907f3dfd,0x7f,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff00009073751c
                        (_DAT_ffff00009083d088,*(undefined4 *)(&DAT_ffff000090803da4 + uVar6 * 0x18)
                         ,1);
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_clear_BAT_d_IMP_ffff0000907e735f,
                   s_mt6379_imp_conv_ffff0000907f3dfd,0x85,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff00009073751c
                        (_DAT_ffff00009083d088,
                         *(undefined4 *)(&DAT_ffff000090803da0 + (ulong)param_3 * 0x18),1);
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_enable_BAT_d_IM_ffff0000907d248a,
                   s_mt6379_imp_conv_ffff0000907f3dfd,0x8b,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff00009073751c(_DAT_ffff00009083d088,uVar1,1);
      if (iVar3 != 0) {
        FUN_ffff00009077e11c
                  (s__s__d__Failed_to_enable_BAT_d_AU_ffff0000907e409b,
                   s_mt6379_imp_conv_ffff0000907f3dfd,0x92,param_3 + 1);
        return iVar3;
      }
      FUN_ffff00009077cea0(6000);
      uVar4 = FUN_ffff0000907065dc(param_3);
      if ((uVar4 & 1) != 0) {
        uVar4 = 0x1e;
LAB_ffff000090706348:
        iVar3 = FUN_ffff0000907375c4
                          (_DAT_ffff00009083d088,
                           *(undefined4 *)(&DAT_ffff000090803dac + uVar6 * 0x18),local_70,2);
        uVar2 = _DAT_ffff00009083d088;
        if (iVar3 != 0) {
          FUN_ffff00009077e11c
                    (s__s__d__Failed_to_read_BAT_d_IMP_v_ffff0000907d56c4,
                     s_mt6379_imp_conv_ffff0000907f3dfd,0xa3,param_3 + 1);
          return iVar3;
        }
        *param_1 = ((local_70[0] & 0x7fff) * 0x1cc0 >> 0xf) * 10;
        iVar3 = FUN_ffff00009073751c(uVar2,uVar1,0);
        if (iVar3 != 0) {
          FUN_ffff00009077e11c
                    (s__s__d__Failed_to_disable_BAT_d_A_ffff0000907f3e0d,
                     s_mt6379_imp_conv_ffff0000907f3dfd,0xad,param_3 + 1);
          return iVar3;
        }
        FUN_ffff00009071ed4c(param_2,param_3);
        FUN_ffff00009077e11c
                  (s__s__d__BAT_d_FG_imp_vbat___d_0_1_ffff0000907f5bcc,
                   s_mt6379_imp_conv_ffff0000907f3dfd,0xb3,param_3 + 1,*param_1,*param_2,
                   0x1e - uVar4);
        return 0;
      }
      lVar5 = FUN_ffff0000907168b0();
      while( true ) {
        uVar4 = FUN_ffff0000907168b0();
        local_64 = 0;
        iVar3 = FUN_ffff000090737554
                          (_DAT_ffff00009083d088,
                           *(undefined4 *)(&DAT_ffff000090803da8 + uVar6 * 0x18),&local_64);
        if ((iVar3 == 0) && ((local_64 & 1) != 0)) break;
        if (lVar5 + 30000U <= uVar4) {
LAB_ffff0000907063fc:
          FUN_ffff00009077e11c
                    (s__s__d__BAT_d_IMP_channel_timeout_ffff0000907e8b99,
                     s_mt6379_imp_conv_ffff0000907f3dfd,0x9c,param_3 + 1,0x1e);
          return -0xd;
        }
      }
      if (lVar5 + 30000U <= uVar4) {
        uVar4 = 1;
        goto LAB_ffff000090706348;
      }
      uVar4 = (lVar5 - uVar4) + 30999;
      if (999 < uVar4) {
        uVar4 = uVar4 / 1000;
        goto LAB_ffff000090706348;
      }
      goto LAB_ffff0000907063fc;
    }
  }
  return iVar3;
}


