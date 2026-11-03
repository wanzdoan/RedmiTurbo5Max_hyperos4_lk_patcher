// ffff00009076ac94  size=1252  FUN_ffff00009076ac94

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009076ac94(long param_1)

{
  uint uVar1;
  undefined8 uVar2;
  uint uVar3;
  int iVar4;
  int iVar5;
  char *pcVar6;
  int iVar7;
  int iVar8;
  undefined1 uVar9;
  ulong uVar10;
  undefined8 local_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined4 local_f0;
  undefined4 uStack_ec;
  undefined8 local_e8;
  undefined4 local_e0;
  undefined8 local_d8;
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
  undefined4 local_70;
  undefined4 local_68;
  undefined4 uStack_64;
  byte local_60 [16];
  
  local_d8 = 0;
  local_e0 = 0x108;
  local_e8 = DAT_ffff00009080e2dc;
  local_f0 = 0;
  uStack_ec = 0x33700;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_128 = 0;
  if (*(int *)(param_1 + 0x1c) == 1) {
    (*_DAT_ffff00009087ccd0)(&local_e8,3,1);
    (*_DAT_ffff00009087ccd0)(&uStack_ec,1,1);
    (*_DAT_ffff00009087cd00)(0xbf,&local_d8,3);
    iVar4 = local_d8._1_1_ + 0xb3;
    uVar1 = (uint)(byte)local_d8 * 0xf + 0xff0;
    iVar5 = local_d8._2_1_ + 0xbe;
    uVar9 = (undefined1)((uint)iVar5 >> 8);
    uVar3 = uVar1 / 10;
    local_d8 = CONCAT62(CONCAT51(local_d8._3_5_,(char)((uint)iVar4 >> 8)),
                        CONCAT11((char)(uVar1 * 0x199a >> 0x10),(char)(uVar1 / 0xa00)));
    iVar7 = iVar4;
    iVar8 = iVar5;
  }
  else {
    uVar3 = 0;
    iVar7 = 0;
    iVar8 = 0;
    iVar5 = 6;
    uVar9 = 5;
    iVar4 = 4;
    local_d8 = 0x30201;
  }
  uVar2 = local_d8;
  local_d8._6_2_ = SUB82(uVar2,6);
  local_d8._0_6_ = CONCAT15((char)iVar5,CONCAT14(uVar9,CONCAT13((char)iVar4,(undefined3)local_d8)));
  FUN_ffff00009077de54
            (s__LK_LCM__s__Lux_0x_04hx__Wx_0x_0_ffff000090801499,s_lcm_get_wp_info_ffff0000907d9c8d,
             uVar3,iVar7,iVar8);
  FUN_ffff00009077f434
            (&local_128,0x3c,s_androidboot_oled_wp__02hhx_02hhx_ffff0000907e020a,local_d8 & 0xff,
             local_d8._1_1_,local_d8._2_1_,local_d8._3_1_,local_d8._4_1_,local_d8._5_1_);
  FUN_ffff000090718f34(&local_128);
  FUN_ffff00009077de54(s__LK_LCM__s____ffff0000907eb62c,s_lcm_read_sn_id_ffff0000907d1ce9);
  local_60[8] = 0;
  local_60[9] = 0;
  local_60[10] = 0;
  local_60[0xb] = 0;
  local_60[0xc] = 0;
  local_60[0xd] = 0;
  local_60[0] = 0;
  local_60[1] = 0;
  local_60[2] = 0;
  local_60[3] = 0;
  local_60[4] = 0;
  local_60[5] = 0;
  local_60[6] = 0;
  local_60[7] = 0;
  local_68 = 0x33700;
  uStack_64 = 0xa3700;
  local_70 = 0;
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
  FUN_ffff00009077de54
            (s__LK_LCM__LCM__s__read_begin_ffff0000907e38a2,s_lcm_read_sn_id_ffff0000907d1ce9);
  (*_DAT_ffff00009087ccd0)(&local_68,1,1);
  (*_DAT_ffff00009087cd00)(0xac,local_60 + 10,3);
  (*_DAT_ffff00009087ccd0)(&uStack_64,1,1);
  (*_DAT_ffff00009087cd00)(0xac,local_60,10);
  uVar10 = 0;
  do {
    FUN_ffff00009077de54
              (s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e0243,s_lcm_read_sn_id_ffff0000907d1ce9
               ,uVar10 & 0xffffffff,local_60[uVar10]);
    pcVar6 = s__LK_LCM__LCM__s__rx_buf__d_____0_ffff0000907e1dc0;
    if ((ulong)local_60[uVar10] < 0x23) {
      local_60[uVar10] = (&DAT_ffff00009080e2f4)[local_60[uVar10]];
      pcVar6 = s__LK_LCM__LCM__s__rx_buf__d_____c_ffff0000907f7062;
    }
    FUN_ffff00009077de54(pcVar6,s_lcm_read_sn_id_ffff0000907d1ce9,uVar10 & 0xffffffff);
    uVar10 = uVar10 + 1;
  } while (uVar10 != 0xe);
  FUN_ffff00009077f434
            (&local_d0,100,s_androidboot_panel_cell_id__c_c_c_ffff0000907fc231,local_60[0],
             local_60[1],local_60[2],local_60[3],local_60[4],local_60[5],local_60[6],local_60[7],
             local_60[8],local_60[9],local_60[10],local_60[0xb],local_60[0xc],local_60[0xd]);
  FUN_ffff000090718f34(&local_d0);
  FUN_ffff00009077eb88(&local_d0,0,100);
  uVar3 = FUN_ffff00009077f38c(&local_d0,s_panel_p10u_42_02_0a_dsc_vdo_pane_ffff0000907d36d9);
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[0]);
  iVar5 = FUN_ffff00009077f38c
                    ((long)&local_d0 + (ulong)(iVar4 + uVar3),&DAT_ffff0000907e38bf,local_60[1]);
  uVar3 = iVar5 + iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[2]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[3]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[4]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[5]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[6]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[7]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[8]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[9]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[10]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[0xb]);
  uVar3 = iVar4 + uVar3;
  iVar4 = FUN_ffff00009077f38c((long)&local_d0 + (ulong)uVar3,&DAT_ffff0000907e38bf,local_60[0xc]);
  FUN_ffff00009077f38c((long)&local_d0 + (ulong)(iVar4 + uVar3),&DAT_ffff0000907e38bf,local_60[0xd])
  ;
  FUN_ffff000090718f34(&local_d0);
  FUN_ffff00009077de54(s__LK_LCM__LCM_cmdline__s_ffff0000907f03b4,&local_d0);
  FUN_ffff00009077de54(s__LK_LCM__s_lh_____ffff0000907e01e5,s_lcm_read_sn_id_ffff0000907d1ce9);
  FUN_ffff00009077eb88(&local_128,0,0x3c);
  FUN_ffff00009077f434
            (&local_128,0x3c,s_panel_p10u_42_02_0a_dsc_vdo_oled_ffff0000907f1bfb,local_d8 & 0xff,
             local_d8._1_1_,local_d8._2_1_,local_d8._3_1_,local_d8._4_1_,local_d8._5_1_);
  FUN_ffff000090718f34(&local_128);
  return;
}


