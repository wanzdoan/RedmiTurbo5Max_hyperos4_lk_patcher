
void _GLOBAL__sub_I_mi_screen_ui_cpp(void)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined *puVar7;
  char *pcVar8;
  string *psVar9;
  ushort local_68 [8];
  void *local_58;
  base local_50;
  undefined7 local_4f;
  char cStack_48;
  undefined3 uStack_47;
  uint uStack_44;
  char cStack_40;
  undefined2 uStack_3f;
  undefined5 uStack_3d;
  long local_38;

  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  pcVar8 = operator_new(0x30);
  uVar6 = s__sys_bus_platform_drivers_ufshcd_00158381._16_8_;
  uVar5 = s__sys_bus_platform_drivers_ufshcd_00158381._8_8_;
  uVar4 = s__sys_bus_platform_drivers_ufshcd_00158381._0_8_;
  uVar2 = CONCAT26(s__sys_bus_platform_drivers_ufshcd_00158381._30_2_,
                   s__sys_bus_platform_drivers_ufshcd_00158381._24_6_);
  pcVar8[0x26] = '\0';
  psVar9 = (string *)&DAT_0042ce00;
  DAT_0057b3a8 = 0x31;
  DAT_0057b3b0 = 0x26;
  uVar3 = CONCAT62(s__sys_bus_platform_drivers_ufshcd_00158381._32_6_,
                   s__sys_bus_platform_drivers_ufshcd_00158381._30_2_);
  DAT_0057b3b8 = pcVar8;
  *(undefined8 *)(pcVar8 + 8) = uVar5;
  *(undefined8 *)pcVar8 = uVar4;
  *(undefined8 *)(pcVar8 + 0x18) = uVar2;
  *(undefined8 *)(pcVar8 + 0x10) = uVar6;
  *(undefined8 *)(pcVar8 + 0x1e) = uVar3;
  puVar7 = PTR__string_00429ac0;
  __cxa_atexit(PTR__string_00429ac0,&DAT_0057b3a8,&DAT_0042ce00);
  pcVar8 = operator_new(0x30);
  pcVar8[0x25] = '\0';
  uVar5 = s__sys_bus_platform_drivers_ufshcd_0014eb74._16_8_;
  uVar4 = s__sys_bus_platform_drivers_ufshcd_0014eb74._0_8_;
  uVar2 = CONCAT35(s__sys_bus_platform_drivers_ufshcd_0014eb74._29_3_,
                   s__sys_bus_platform_drivers_ufshcd_0014eb74._24_5_);
  DAT_0057b3c8 = 0x25;
  DAT_0057b3c0 = 0x31;
  uVar3 = CONCAT53(s__sys_bus_platform_drivers_ufshcd_0014eb74._32_5_,
                   s__sys_bus_platform_drivers_ufshcd_0014eb74._29_3_);
  DAT_0057b3d0 = pcVar8;
  *(undefined8 *)(pcVar8 + 8) = s__sys_bus_platform_drivers_ufshcd_0014eb74._8_8_;
  *(undefined8 *)pcVar8 = uVar4;
  *(undefined8 *)(pcVar8 + 0x18) = uVar2;
  *(undefined8 *)(pcVar8 + 0x10) = uVar5;
  *(undefined8 *)(pcVar8 + 0x1d) = uVar3;
  __cxa_atexit(puVar7,&DAT_0057b3c0,&DAT_0042ce00);
  pcVar8 = operator_new(0x30);
  pcVar8[0x27] = '\0';
  uVar5 = s__sys_bus_platform_drivers_ufshcd_0014eb9a._16_8_;
  uVar4 = s__sys_bus_platform_drivers_ufshcd_0014eb9a._0_8_;
  uVar2 = CONCAT17(s__sys_bus_platform_drivers_ufshcd_0014eb9a[0x1f],
                   s__sys_bus_platform_drivers_ufshcd_0014eb9a._24_7_);
  DAT_0057b3e0 = 0x27;
  DAT_0057b3d8 = 0x31;
  uVar3 = CONCAT71(s__sys_bus_platform_drivers_ufshcd_0014eb9a._32_7_,
                   s__sys_bus_platform_drivers_ufshcd_0014eb9a[0x1f]);
  DAT_0057b3e8 = pcVar8;
  *(undefined8 *)(pcVar8 + 8) = s__sys_bus_platform_drivers_ufshcd_0014eb9a._8_8_;
  *(undefined8 *)pcVar8 = uVar4;
  *(undefined8 *)(pcVar8 + 0x18) = uVar2;
  *(undefined8 *)(pcVar8 + 0x10) = uVar5;
  *(undefined8 *)(pcVar8 + 0x1f) = uVar3;
  __cxa_atexit(puVar7);
  uStack_44 = uStack_44 & 0xffffff00;
  local_68[0] = 0;
  local_50 = (base)0x16;
  local_4f = (undefined7)s_ro_boot_hwc_0015318a._0_8_;
  cStack_48 = SUB81(s_ro_boot_hwc_0015318a._0_8_,7);
  _cStack_48 = CONCAT31(0x637768,cStack_48);
  android::base::GetProperty(&local_50,(string *)local_68,psVar9);
  if ((local_68[0] & 1) != 0) {
    operator_delete(local_58);
  }
  if (((byte)local_50 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_3d,CONCAT21(uStack_3f,cStack_40)));
  }
  psVar9 = (string *)&DAT_0042ce00;
  __cxa_atexit(PTR__string_00429ac0,&DAT_0057b460);
  local_68[0] = 0;
  local_50 = (base)0x22;
  uStack_47 = (undefined3)s_ro_product_device_0014d22f._8_8_;
  uStack_44 = SUB84(s_ro_product_device_0014d22f._8_8_,3);
  cStack_40 = SUB81(s_ro_product_device_0014d22f._8_8_,7);
  local_4f = (undefined7)s_ro_product_device_0014d22f._0_8_;
  cStack_48 = SUB81(s_ro_product_device_0014d22f._0_8_,7);
  uStack_3f = 0x65;
  android::base::GetProperty(&local_50,(string *)local_68,psVar9);
  if ((local_68[0] & 1) != 0) {
    operator_delete(local_58);
  }
  if (((byte)local_50 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_3d,CONCAT21(uStack_3f,cStack_40)));
  }
  __cxa_atexit(PTR__string_00429ac0,&DAT_0057b478,&DAT_0042ce00);
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
