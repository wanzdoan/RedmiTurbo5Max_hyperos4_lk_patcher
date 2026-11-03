
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073c870(undefined8 param_1)

{
  int iVar1;
  uint uVar2;
  undefined8 *puVar3;
  char *pcVar4;
  ulong uVar5;
  ulong *puVar6;
  undefined4 uVar8;
  undefined8 uVar7;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  ulong local_50;
  ulong local_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  puVar6 = &local_50;
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  local_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  local_88 = 0;
  local_a0 = 0;
  local_98 = 0;
  local_b0 = 0;
  local_a8 = 0;
  local_c0 = 0;
  local_b8 = 0;
  uStack_c8 = 0;
  FUN_ffff00009071ebf4(0xc200041c,1,0,0,0,0,0,0,puVar6);
  uVar8 = (undefined4)((ulong)puVar6 >> 0x20);
  FUN_ffff00009077e11c
            (s__s__DFD_SOC_STATUS___0x_X_ffff0000907e958f,s_dfd_soc_init_ffff0000907ec70f,
             local_48 & 0xffffffff);
  puVar3 = (undefined8 *)FUN_ffff00009073deb8(0xdb45);
  if (puVar3 == (undefined8 *)0x0) {
    pcVar4 = s__s__sram_flag____NULL_ffff0000907e638c;
  }
  else {
    *puVar3 = 0x5566520;
    iVar1 = FUN_ffff00009073cac8(param_1);
    if (iVar1 < 1) {
      return;
    }
    DAT_ffff00009086e150 =
         FUN_ffff0000907198dc
                   (DAT_ffff00009086e158,_DAT_ffff00009086e160,DAT_ffff00009086e168,0,0,
                    s_platform_debug_dfdsoc_dump_ffff0000907eae4e);
    if (DAT_ffff00009086e150 != 0) {
      uVar2 = FUN_ffff0000907258a8(param_1,DAT_ffff00009086e150,&uStack_c8);
      if ((int)uVar2 < 0) {
        uVar5 = (ulong)uVar2;
        pcVar4 = s__s__emi_addr2dram_v2_fail__d__ffff0000907d46dd;
      }
      else {
        FUN_ffff00009077e11c
                  (s__s__dram_buffer_0x_llX___>_rank__ffff0000907fba04,
                   s_dfd_soc_init_ffff0000907ec70f,DAT_ffff00009086e150,local_c0 & 0xffffffff,
                   local_b8 & 0xffffffff,local_c0._4_4_,local_b8._4_4_);
        FUN_ffff00009077e11c
                  (s__s__row_width__u___bank_width__u_ffff0000907e63a3,
                   s_dfd_soc_init_ffff0000907ec70f,local_b0._4_4_,local_b0 & 0xffffffff,
                   local_a8 & 0xffffffff,local_a8._4_4_,local_a0 & 0xffffffff);
        FUN_ffff00009077e11c(s_chn_scrm_en__u__ffff0000907cf8c0,local_a0._4_4_);
        uVar7 = CONCAT44(uVar8,(undefined4)local_80);
        FUN_ffff00009077e11c
                  (s__s__cen_scrm____0___X___1___X____ffff0000907e4b85,
                   s_dfd_soc_init_ffff0000907ec70f,local_98 & 0xffffffff,local_98._4_4_,
                   local_90 & 0xffffffff,local_90._4_4_,local_88 & 0xffffffff,local_88._4_4_,uVar7);
        uVar8 = (undefined4)((ulong)uVar7 >> 0x20);
        FUN_ffff00009077e11c
                  (s__7___X___8___X___9___X___10___X_ffff0000907f1596,local_80._4_4_,
                   uStack_78 & 0xffffffff,uStack_78._4_4_,local_70 & 0xffffffff);
        FUN_ffff00009077e11c
                  (s__s__chn_scrm____0___X___1___X____ffff0000907f4a23,
                   s_dfd_soc_init_ffff0000907ec70f,local_70._4_4_,uStack_68 & 0xffffffff,
                   uStack_68._4_4_,local_60 & 0xffffffff,local_60._4_4_,uStack_58 & 0xffffffff,
                   CONCAT44(uVar8,uStack_58._4_4_));
        FUN_ffff00009071ebf4
                  (0xc200041c,0,(long)(int)local_b8,(long)(int)local_b0,(long)(int)local_a8,0,0,0,
                   &local_50);
        uVar8 = DAT_ffff00009086e148;
        uVar5 = local_50 & 0xffffffff;
        if (-1 < (int)local_50) {
          *(int *)((long)puVar3 + 4) = (int)DAT_ffff00009086e150;
          FUN_ffff000090739ec0(uVar8);
          return;
        }
        pcVar4 = s__s__dfd_soc_smc_setup_fail__d__ffff0000907ff22b;
      }
      FUN_ffff00009077e11c(pcVar4,s_dfd_soc_init_ffff0000907ec70f,uVar5);
      return;
    }
    pcVar4 = s__s__reserve_dfd_soc_buffer_faile_ffff0000907d2deb;
    DAT_ffff00009086e150 = 0;
  }
  FUN_ffff00009077e11c(pcVar4,s_dfd_soc_init_ffff0000907ec70f);
  return;
}


