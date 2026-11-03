// ffff0000907061c0  size=964  FUN_ffff0000907061c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907061c0(int *param_1,undefined4 *param_2,uint param_3)

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
  
  uVar2 = _DAT_ffff00009083d0e8;
  local_70[0] = 0;
  if (1 < param_3) {
    FUN_ffff00009077de54
              (s__s__d__bat_idx__u__BUG_ffff0000907ebc48,s_mt6379_imp_conv_ffff0000907f3c71,0x6d,
               param_3);
    return -7;
  }
  local_6c = 0;
  uVar1 = *(undefined4 *)(&DAT_ffff000090803b8c + (ulong)param_3 * 0x18);
  iVar3 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,uVar1,&local_6c);
  if (-1 < iVar3) {
    local_6c = local_6c & 0xfffffff3;
    iVar3 = FUN_ffff000090737284(uVar2,uVar1);
    uVar2 = _DAT_ffff00009083d0e8;
    if (iVar3 != 0) {
      return iVar3;
    }
    local_68 = 0;
    iVar3 = FUN_ffff0000907372bc(_DAT_ffff00009083d0e8,uVar1,&local_68);
    if (-1 < iVar3) {
      local_68 = local_68 & 0xfffffffc;
      iVar3 = FUN_ffff000090737284(uVar2,uVar1);
      if (iVar3 != 0) {
        return iVar3;
      }
      uVar6 = (ulong)param_3;
      uVar1 = *(undefined4 *)(&DAT_ffff000090803b88 + uVar6 * 0x18);
      iVar3 = FUN_ffff000090737284(_DAT_ffff00009083d0e8,uVar1,0);
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_clear_BAT_d_AUX_ffff0000907d8aee,
                   s_mt6379_imp_conv_ffff0000907f3c71,0x7f,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff000090737284
                        (_DAT_ffff00009083d0e8,*(undefined4 *)(&DAT_ffff000090803b7c + uVar6 * 0x18)
                         ,1);
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_clear_BAT_d_IMP_ffff0000907e721b,
                   s_mt6379_imp_conv_ffff0000907f3c71,0x85,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff000090737284
                        (_DAT_ffff00009083d0e8,
                         *(undefined4 *)(&DAT_ffff000090803b78 + (ulong)param_3 * 0x18),1);
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_enable_BAT_d_IM_ffff0000907d244d,
                   s_mt6379_imp_conv_ffff0000907f3c71,0x8b,param_3 + 1);
        return iVar3;
      }
      iVar3 = FUN_ffff000090737284(_DAT_ffff00009083d0e8,uVar1,1);
      if (iVar3 != 0) {
        FUN_ffff00009077de54
                  (s__s__d__Failed_to_enable_BAT_d_AU_ffff0000907e3fca,
                   s_mt6379_imp_conv_ffff0000907f3c71,0x92,param_3 + 1);
        return iVar3;
      }
      FUN_ffff00009077cbd8(6000);
      uVar4 = FUN_ffff000090706680(param_3);
      if ((uVar4 & 1) != 0) {
        uVar4 = 0x1e;
LAB_ffff0000907063ec:
        iVar3 = FUN_ffff00009073732c
                          (_DAT_ffff00009083d0e8,
                           *(undefined4 *)(&DAT_ffff000090803b84 + uVar6 * 0x18),local_70,2);
        uVar2 = _DAT_ffff00009083d0e8;
        if (iVar3 != 0) {
          FUN_ffff00009077de54
                    (s__s__d__Failed_to_read_BAT_d_IMP_v_ffff0000907d5687,
                     s_mt6379_imp_conv_ffff0000907f3c71,0xa3,param_3 + 1);
          return iVar3;
        }
        *param_1 = ((local_70[0] & 0x7fff) * 0x1cc0 >> 0xf) * 10;
        iVar3 = FUN_ffff000090737284(uVar2,uVar1,0);
        if (iVar3 != 0) {
          FUN_ffff00009077de54
                    (s__s__d__Failed_to_disable_BAT_d_A_ffff0000907f3c81,
                     s_mt6379_imp_conv_ffff0000907f3c71,0xad,param_3 + 1);
          return iVar3;
        }
        FUN_ffff00009071ebc4(param_2,param_3);
        FUN_ffff00009077de54
                  (s__s__d__BAT_d_FG_imp_vbat___d_0_1_ffff0000907f5a55,
                   s_mt6379_imp_conv_ffff0000907f3c71,0xb3,param_3 + 1,*param_1,*param_2,
                   0x1e - uVar4);
        return 0;
      }
      lVar5 = FUN_ffff00009071676c();
      while( true ) {
        uVar4 = FUN_ffff00009071676c();
        local_64 = 0;
        iVar3 = FUN_ffff0000907372bc
                          (_DAT_ffff00009083d0e8,
                           *(undefined4 *)(&DAT_ffff000090803b80 + uVar6 * 0x18),&local_64);
        if ((iVar3 == 0) && ((local_64 & 1) != 0)) break;
        if (lVar5 + 30000U <= uVar4) {
LAB_ffff0000907064a0:
          FUN_ffff00009077de54
                    (s__s__d__BAT_d_IMP_channel_timeout_ffff0000907e8a55,
                     s_mt6379_imp_conv_ffff0000907f3c71,0x9c,param_3 + 1,0x1e);
          return -0xd;
        }
      }
      if (lVar5 + 30000U <= uVar4) {
        uVar4 = 1;
        goto LAB_ffff0000907063ec;
      }
      uVar4 = (lVar5 - uVar4) + 30999;
      if (999 < uVar4) {
        uVar4 = uVar4 / 1000;
        goto LAB_ffff0000907063ec;
      }
      goto LAB_ffff0000907064a0;
    }
  }
  return iVar3;
}


