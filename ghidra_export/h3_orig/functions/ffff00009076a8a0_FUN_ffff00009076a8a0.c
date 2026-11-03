// ffff00009076a8a0  size=316  FUN_ffff00009076a8a0

bool FUN_ffff00009076a8a0(void)

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
  
  FUN_ffff00009077de54(s__LK_LCM__s__enter_ffff0000907f8bd1,s_lcm_compare_id_ffff0000907e5323);
  FUN_ffff000090785468(0xb8,0);
  FUN_ffff000090785354(0xb8,0);
  FUN_ffff000090785468(0xb7,0);
  FUN_ffff000090785354(0xb7,0);
  uVar1 = FUN_ffff000090785424(0xb8);
  iVar2 = FUN_ffff000090785424(0xb7);
  uVar1 = uVar1 | iVar2 << 2;
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s_GPIO_PULL_DOWN__m_ffff0000907f702e,s_lcm_compare_id_ffff0000907e5323,
             uVar1);
  if (uVar1 != 4) {
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s__p10u_36_02_0b_d_ffff0000907fa5e7,s_lcm_compare_id_ffff0000907e5323
              );
    FUN_ffff00009077de54(s__LK_LCM___s_exit_ffff0000907d1cd7,s_lcm_compare_id_ffff0000907e5323);
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
              (s__LK_LCM__LCM__s__p10u_36_02_0b_d_ffff0000907e6a3b,s_lcm_compare_id_ffff0000907e5323
              );
    FUN_ffff00009077f434
              (&local_e8,200,&DAT_ffff0000907dbe71,
               s_panel_name_dsi_p10u_36_02_0b_dsc_ffff0000907ecfb6);
    FUN_ffff000090718f34(&local_e8);
  }
  return uVar1 == 4;
}


