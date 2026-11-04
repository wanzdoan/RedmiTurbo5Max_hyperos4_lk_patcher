
/* WARNING: Type propagation algorithm not settling */
/* AskToReboot(Device*, Device::BuiltinAction) */

bool AskToReboot(long *param_1,int param_2,string *param_3)

{
  string *psVar1;
  undefined4 uVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  string *psVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  bool bVar9;
  uint uVar10;
  char *pcVar11;
  ulong *puVar12;
  long *plVar13;
  long lVar14;
  uint *puVar15;
  undefined4 *puVar16;
  ostream *poVar17;
  undefined1 uVar18;
  long lVar19;
  ulong uVar20;
  string *psVar21;
  string *local_140;
  string *local_138;
  string *local_130;
  string *local_128;
  string *local_120;
  string *local_118;
  byte local_110;
  undefined7 uStack_10f;
  char cStack_108;
  undefined2 uStack_107;
  undefined5 uStack_105;
  char *local_100;
  string local_f8;
  char cStack_f7;
  undefined6 uStack_f6;
  char cStack_f0;
  undefined2 local_ef;
  undefined5 uStack_ed;
  void *local_e8;
  string *local_e0;
  string *local_d8;
  string **local_d0;
  undefined8 uStack_c8;
  base local_c0;
  undefined7 uStack_bf;
  char cStack_b8;
  undefined7 uStack_b7;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  uint local_a0;
  undefined4 uStack_9c;
  char cStack_98;
  undefined5 uStack_97;
  undefined2 uStack_92;
  char cStack_90;
  undefined5 uStack_8f;
  undefined1 uStack_8a;
  undefined1 uStack_89;
  long *local_88;
  uint *puStack_80;
  char *local_78;
  byte local_70;
  undefined7 uStack_6f;
  char cStack_68;
  undefined7 uStack_67;
  char cStack_60;
  undefined2 uStack_5f;
  undefined5 uStack_5d;
  long local_58;

  lVar3 = tpidr_el0;
  local_58 = *(long *)(lVar3 + 0x28);
  uStack_b0._4_4_ = uStack_b0._4_4_ & 0xffffff00;
  local_c0 = (base)0x26;
  uStack_b7 = (undefined7)s_ro_boot_slot_suffix_0015771b._8_8_;
  uStack_b0._0_4_ = CONCAT31(0x786966,SUB81(s_ro_boot_slot_suffix_0015771b._8_8_,7));
  uStack_bf = (undefined7)s_ro_boot_slot_suffix_0015771b._0_8_;
  cStack_b8 = SUB81(s_ro_boot_slot_suffix_0015771b._0_8_,7);
  local_f8 = (string)0x0;
  cStack_f7 = '\0';
  android::base::GetProperty(&local_c0,&local_f8,param_3);
  uVar18 = SUB81(param_3,0);
  uVar20 = (ulong)((byte)local_a0._0_1_ >> 1);
  if (((byte)local_a0._0_1_ & 1) != 0) {
    uVar20 = CONCAT26(uStack_92,CONCAT51(uStack_97,cStack_98));
  }
  if (((byte)local_a0._0_1_ & 1) != 0) {
    operator_delete((void *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))));
  }
  if (((byte)local_f8 & 1) != 0) {
    operator_delete(local_e8);
  }
  if (((byte)local_c0 & 1) != 0) {
    operator_delete((void *)CONCAT44(uStack_b0._4_4_,(undefined4)uStack_b0));
  }
  uStack_8a = 0;
  uStack_97._0_1_ = s_ro_virtual_ab_enabled_001583fa[8];
  uStack_97._1_1_ = s_ro_virtual_ab_enabled_001583fa[9];
  uStack_97._2_1_ = s_ro_virtual_ab_enabled_001583fa[10];
  uStack_97._3_1_ = s_ro_virtual_ab_enabled_001583fa[0xb];
  uStack_97._4_1_ = s_ro_virtual_ab_enabled_001583fa[0xc];
  local_a0 = CONCAT31((int3)s_ro_virtual_ab_enabled_001583fa._0_8_,0x2a);
  uStack_9c = SUB84(s_ro_virtual_ab_enabled_001583fa._0_8_,3);
  cStack_98 = SUB81(s_ro_virtual_ab_enabled_001583fa._0_8_,7);
  uStack_92 = (undefined2)s_ro_virtual_ab_enabled_001583fa._13_3_;
  cStack_90 = SUB31(s_ro_virtual_ab_enabled_001583fa._13_3_,2);
  uStack_8f._0_1_ = s_ro_virtual_ab_enabled_001583fa[0x10];
  uStack_8f._1_1_ = s_ro_virtual_ab_enabled_001583fa[0x11];
  uStack_8f._2_1_ = s_ro_virtual_ab_enabled_001583fa[0x12];
  uStack_8f._3_1_ = s_ro_virtual_ab_enabled_001583fa[0x13];
  uStack_8f._4_1_ = s_ro_virtual_ab_enabled_001583fa[0x14];
  uVar10 = android::base::GetBoolProperty((base *)&local_a0,(string *)0x0,(bool)uVar18);
  if ((local_a0 & 1) != 0) {
    operator_delete((void *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))));
  }
  if (uVar20 != 0 && (uVar10 & 1) == 0) {
    bVar9 = true;
    goto LAB_0022e34c;
  }
  local_f8 = (string)0x0;
  cStack_f7 = '\0';
  uStack_f6 = 0;
  cStack_f0 = '\0';
  local_ef = 0;
  uStack_ed = 0;
  local_e8 = (void *)0x0;
  local_110 = 0;
  uStack_10f = 0;
  cStack_108 = '\0';
  uStack_107 = 0;
  uStack_105 = 0;
  local_100 = (char *)0x0;
  if (param_2 == 8) {
    uVar20 = 9;
    local_ef = 0x66;
    local_f8 = (string)0x12;
    local_110 = 0x14;
    cStack_f7 = (char)s_power_off_001474b3._0_8_;
    uStack_f6 = SUB86(s_power_off_001474b3._0_8_,1);
    cStack_f0 = SUB81(s_power_off_001474b3._0_8_,7);
    uStack_107 = 0x6666;
    uStack_10f = (undefined7)s_Power_off_00156983._0_8_;
    cStack_108 = SUB81(s_Power_off_00156983._0_8_,7);
  }
  else {
    uVar20 = android::base::ShouldLog(6,0);
    if ((uVar20 & 1) == 0) {
      uVar20 = 0;
    }
    else {
      puVar16 = (undefined4 *)__errno();
      uVar2 = *puVar16;
      android::base::LogMessage::LogMessage
                ((LogMessage *)&local_a0,"bootable/recovery/recovery.cpp",0x1e2,6,0,0xffffffff);
      poVar17 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_a0);
      poVar17 = std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                          (poVar17,"Invalid chosen action ",0x16);
      std::ostream::operator<<(poVar17,param_2);
      android::base::LogMessage::~LogMessage((LogMessage *)&local_a0);
      uVar20 = 0;
      *puVar16 = uVar2;
    }
  }
  local_128 = (string *)0x0;
  local_120 = (string *)0x0;
  local_118 = (string *)0x0;
  pcVar11 = operator_new(0x30);
  uVar8 = s_WARNING__Previous_installation_h_00149a48._16_8_;
  uVar7 = s_WARNING__Previous_installation_h_00149a48._8_8_;
  uVar5 = s_WARNING__Previous_installation_h_00149a48._0_8_;
  cStack_90 = (char)pcVar11;
  uStack_8f = (undefined5)((ulong)pcVar11 >> 8);
  uStack_8a = (undefined1)((ulong)pcVar11 >> 0x30);
  uStack_89 = (undefined1)((ulong)pcVar11 >> 0x38);
  uVar4 = CONCAT62(s_WARNING__Previous_installation_h_00149a48._26_6_,
                   s_WARNING__Previous_installation_h_00149a48._24_2_);
  local_a0 = 0x31;
  uStack_9c = 0;
  cStack_98 = 0x2a;
  uStack_97 = 0;
  uStack_92 = 0;
  pcVar11[0x2a] = '\0';
  *(undefined8 *)(pcVar11 + 8) = uVar7;
  *(undefined8 *)pcVar11 = uVar5;
  *(undefined8 *)(pcVar11 + 0x18) = uVar4;
  *(undefined8 *)(pcVar11 + 0x10) = uVar8;
  uVar4 = CONCAT26(s_WARNING__Previous_installation_h_00149a48._32_2_,
                   s_WARNING__Previous_installation_h_00149a48._26_6_);
  *(undefined8 *)(pcVar11 + 0x22) = s_WARNING__Previous_installation_h_00149a48._34_8_;
  *(undefined8 *)(pcVar11 + 0x1a) = uVar4;
  local_130 = operator_new(0x30);
  uVar8 = s_Your_device_may_fail_to_boot_if_y_0014c669._16_8_;
  uVar7 = s_Your_device_may_fail_to_boot_if_y_0014c669._0_8_;
  local_138 = (string *)(uVar20 | 0x26);
  psVar1 = local_130 + 0x26;
  uVar4 = CONCAT26(s_Your_device_may_fail_to_boot_if_y_0014c669._30_2_,
                   s_Your_device_may_fail_to_boot_if_y_0014c669._24_6_);
  local_140 = (string *)0x31;
  uVar5 = CONCAT62(s_Your_device_may_fail_to_boot_if_y_0014c669._32_6_,
                   s_Your_device_may_fail_to_boot_if_y_0014c669._30_2_);
  *(undefined8 *)(local_130 + 8) = s_Your_device_may_fail_to_boot_if_y_0014c669._8_8_;
  *(undefined8 *)local_130 = uVar7;
  *(undefined8 *)(local_130 + 0x18) = uVar4;
  *(undefined8 *)(local_130 + 0x10) = uVar8;
  *(undefined8 *)(local_130 + 0x1e) = uVar5;
  if (param_2 == 8) {
    memcpy(psVar1,(void *)((ulong)&local_f8 | 1),uVar20);
  }
  psVar1[uVar20] = (string)0x0;
  puVar12 = (ulong *)std::string::append((char *)&local_140,0x153c89);
  uVar5 = s_Confirm_reboot__0014dfcf._8_8_;
  uVar4 = s_Confirm_reboot__0014dfcf._0_8_;
  local_78 = (char *)puVar12[2];
  puStack_80 = (uint *)puVar12[1];
  local_88 = (long *)*puVar12;
  local_d0 = &local_128;
  puVar12[1] = 0;
  puVar12[2] = 0;
  *puVar12 = 0;
  local_70 = 0x22;
  uStack_67 = (undefined7)uVar5;
  cStack_60 = SUB81(uVar5,7);
  uStack_6f = (undefined7)uVar4;
  cStack_68 = SUB81(uVar4,7);
  uStack_5f = 0x3f;
  local_120 = (string *)0x0;
  local_118 = (string *)0x0;
  local_128 = (string *)0x0;
  uStack_c8 = 0;
  local_d8 = operator_new(0x48);
  local_118 = local_d8 + 0x48;
  uStack_b0 = &local_d8;
  local_c0 = SUB81(&local_118,0);
  uStack_bf = (undefined7)((ulong)&local_118 >> 8);
  cStack_b8 = (char)&local_e0;
  uStack_b7 = (undefined7)((ulong)&local_e0 >> 8);
  uStack_a8 = 0;
  local_128 = local_d8;
  local_120 = local_d8;
  local_e0 = local_d8;
  if ((local_a0 & 1) == 0) {
    *(ulong *)(local_d8 + 8) = CONCAT26(uStack_92,CONCAT51(uStack_97,cStack_98));
    *(ulong *)local_d8 = CONCAT44(uStack_9c,local_a0);
    *(ulong *)(local_d8 + 0x10) =
         CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90)));
  }
  else {
    std::string::__init_copy_ctor_external
              (local_d8,(char *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))
                                        ),CONCAT26(uStack_92,CONCAT51(uStack_97,cStack_98)));
  }
  psVar1 = local_d8 + 0x18;
  if (((ulong)local_88 & 1) == 0) {
    psVar21 = local_d8 + 0x30;
    *(char **)(local_d8 + 0x28) = local_78;
    *(uint **)(local_d8 + 0x20) = puStack_80;
    *(long **)psVar1 = local_88;
    local_d8 = psVar1;
LAB_0022e05c:
    *(ulong *)(psVar21 + 0x10) = CONCAT53(uStack_5d,CONCAT21(uStack_5f,cStack_60));
    *(ulong *)(psVar21 + 8) = CONCAT71(uStack_67,cStack_68);
    *(ulong *)psVar21 = CONCAT71(uStack_6f,local_70);
  }
  else {
    local_d8 = psVar1;
    std::string::__init_copy_ctor_external(psVar1,local_78,(ulong)puStack_80);
    psVar21 = local_d8 + 0x18;
    local_d8 = psVar21;
    if ((local_70 & 1) == 0) goto LAB_0022e05c;
    std::string::__init_copy_ctor_external
              (psVar21,(char *)CONCAT53(uStack_5d,CONCAT21(uStack_5f,cStack_60)),
               CONCAT71(uStack_67,cStack_68));
    psVar21 = local_d8;
  }
  local_120 = psVar21 + 0x18;
  if ((local_70 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_5d,CONCAT21(uStack_5f,cStack_60)));
  }
  if (((ulong)local_88 & 1) != 0) {
    operator_delete(local_78);
  }
  if ((local_a0 & 1) != 0) {
    operator_delete((void *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))));
  }
  if (((ulong)local_140 & 1) != 0) {
    operator_delete(local_130);
  }
  local_140 = (string *)0x0;
  local_138 = (string *)0x0;
  local_130 = (string *)0x0;
  local_a0 = 0x6143200e;
  uStack_9c = 0x6c65636e;
  cStack_98 = 0;
  if ((local_110 & 1) == 0) {
    puStack_80 = (uint *)CONCAT53(uStack_105,CONCAT21(uStack_107,cStack_108));
    local_88 = (long *)CONCAT71(uStack_10f,local_110);
    local_78 = local_100;
  }
  else {
    std::string::__init_copy_ctor_external
              ((string *)&local_88,local_100,CONCAT53(uStack_105,CONCAT21(uStack_107,cStack_108)));
  }
  local_140 = (string *)0x0;
  local_138 = (string *)0x0;
  local_130 = (string *)0x0;
  uStack_c8 = 0;
  local_d0 = &local_140;
  local_d8 = operator_new(0x30);
  local_130 = local_d8 + 0x30;
  uStack_b0 = &local_d8;
  local_c0 = SUB81(&local_130,0);
  uStack_bf = (undefined7)((ulong)&local_130 >> 8);
  cStack_b8 = (char)&local_e0;
  uStack_b7 = (undefined7)((ulong)&local_e0 >> 8);
  uStack_a8 = 0;
  local_140 = local_d8;
  local_138 = local_d8;
  local_e0 = local_d8;
  if ((local_a0 & 1) == 0) {
    *(ulong *)(local_d8 + 8) = CONCAT26(uStack_92,CONCAT51(uStack_97,cStack_98));
    *(ulong *)local_d8 = CONCAT44(uStack_9c,local_a0);
    *(ulong *)(local_d8 + 0x10) =
         CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90)));
  }
  else {
    std::string::__init_copy_ctor_external
              (local_d8,(char *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))
                                        ),CONCAT26(uStack_92,CONCAT51(uStack_97,cStack_98)));
  }
  psVar1 = local_d8 + 0x18;
  if (((ulong)local_88 & 1) == 0) {
    *(char **)(local_d8 + 0x28) = local_78;
    *(uint **)(local_d8 + 0x20) = puStack_80;
    *(long **)psVar1 = local_88;
    local_d8 = psVar1;
  }
  else {
    local_d8 = psVar1;
    std::string::__init_copy_ctor_external(psVar1,local_78,(ulong)puStack_80);
  }
  local_138 = local_d8 + 0x18;
  if (((ulong)local_88 & 1) != 0) {
    operator_delete(local_78);
  }
  if ((local_a0 & 1) != 0) {
    operator_delete((void *)CONCAT17(uStack_89,CONCAT16(uStack_8a,CONCAT51(uStack_8f,cStack_90))));
  }
  plVar13 = (long *)(**(code **)(*param_1 + 0x10))(param_1);
  cStack_90 = '\x01';
  uStack_8f._0_1_ = '\0';
  uStack_8f._1_1_ = '\0';
  uStack_8f._2_1_ = '\0';
  uStack_8f._3_1_ = '\0';
  uStack_8f._4_1_ = '\0';
  uStack_8a = 0;
  uStack_89 = 0;
  local_a0 = 0x424918;
  uStack_9c = 0;
  cStack_98 = '@';
  uStack_97._0_1_ = '\0';
  uStack_97._1_1_ = '\0';
  uStack_97._2_1_ = '\0';
  uStack_97._3_1_ = '\0';
  uStack_97._4_1_ = '\0';
  uStack_92 = 0;
  local_88 = param_1;
  puStack_80 = &local_a0;
  lVar14 = (**(code **)(*plVar13 + 0x1d0))(plVar13,&local_128,&local_140,0,1,&local_a0);
  if (puStack_80 == &local_a0) {
    lVar19 = 4;
    puVar15 = &local_a0;
LAB_0022e278:
    (**(code **)(*(long *)puVar15 + lVar19 * 8))();
  }
  else if (puStack_80 != (uint *)0x0) {
    lVar19 = 5;
    puVar15 = puStack_80;
    goto LAB_0022e278;
  }
  psVar1 = local_140;
  bVar9 = lVar14 == 1;
  psVar21 = local_138;
  if (local_140 != (string *)0x0) {
    while (psVar6 = psVar21, psVar6 != psVar1) {
      psVar21 = psVar6 + -0x18;
      if (((byte)*psVar21 & 1) != 0) {
        operator_delete(*(void **)(psVar6 + -8));
      }
    }
    local_138 = psVar1;
    operator_delete(local_140);
  }
  psVar1 = local_128;
  psVar21 = local_120;
  if (local_128 != (string *)0x0) {
    while (psVar6 = psVar21, psVar6 != psVar1) {
      psVar21 = psVar6 + -0x18;
      if (((byte)*psVar21 & 1) != 0) {
        operator_delete(*(void **)(psVar6 + -8));
      }
    }
    local_120 = psVar1;
    operator_delete(local_128);
  }
  if ((local_110 & 1) != 0) {
    operator_delete(local_100);
  }
  if (((byte)local_f8 & 1) != 0) {
    operator_delete(local_e8);
  }
LAB_0022e34c:
  if (*(long *)(lVar3 + 0x28) != local_58) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return bVar9;
}
