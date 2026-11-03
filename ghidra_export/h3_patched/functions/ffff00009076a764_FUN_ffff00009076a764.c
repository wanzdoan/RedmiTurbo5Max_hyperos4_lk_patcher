// ffff00009076a764  size=316  FUN_ffff00009076a764

bool FUN_ffff00009076a764(void)

{
  uint uVar1;
  int iVar2;
  undefined8 local_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  FUN_ffff00009077de54(s__LK_LCM__s__enter_ffff0000907f8bd1,s_lcm_compare_id_ffff0000907e530c);
  FUN_ffff000090785468(0xb8,0);
  FUN_ffff000090785354(0xb8,0);
  FUN_ffff000090785468(0xb7,0);
  FUN_ffff000090785354(0xb7,0);
  uVar1 = FUN_ffff000090785424(0xb8);
  iVar2 = FUN_ffff000090785424(0xb7);
  uVar1 = uVar1 | iVar2 << 2;
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s_GPIO_PULL_DOWN__m_ffff0000907f7017,s_lcm_compare_id_ffff0000907e530c,
             uVar1);
  if (uVar1 != 1) {
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s__p10u_42_02_0a_d_ffff0000907e9d7c,s_lcm_compare_id_ffff0000907e530c
              );
    FUN_ffff00009077de54(s__LK_LCM___s_exit_ffff0000907d1cd7,s_lcm_compare_id_ffff0000907e530c);
  }
  else {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
    local_a0 = 0;
    uStack_98 = 0;
    local_b0 = 0;
    uStack_a8 = 0;
    local_c0 = 0;
    uStack_b8 = 0;
    local_d0 = 0;
    uStack_c8 = 0;
    local_e0 = 0;
    uStack_d8 = 0;
    local_e8 = 0;
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s__p10u_42_02_0a_d_ffff0000907fc1ef,s_lcm_compare_id_ffff0000907e530c
              );
    FUN_ffff00009077f434
              (&local_e8,200,&DAT_ffff0000907dbe5a,
               s_panel_name_dsi_p10u_42_02_0a_dsc_ffff0000907d8415);
    FUN_ffff000090718f34(&local_e8);
  }
  return uVar1 == 1;
}


