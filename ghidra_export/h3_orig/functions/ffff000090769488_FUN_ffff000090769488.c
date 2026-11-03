// ffff000090769488  size=352  FUN_ffff000090769488

uint FUN_ffff000090769488(undefined8 param_1,long param_2)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined8 local_48;
  undefined8 uStack_40;
  int local_34;
  
  local_34 = 0;
  local_48 = 0;
  uStack_40 = 0;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  if (param_2 != 0) {
    iVar2 = FUN_ffff00009076e064(param_2,0xffffffff,s_mediatek_mtkfb_ffff0000907d9c7e);
    if (iVar2 < 0) {
      return 0xffffffff;
    }
    iVar3 = FUN_ffff00009076de08(param_2,iVar2,s_pinctrl_names_ffff0000907e386d,param_1);
    if (iVar3 < 0) {
      return 0xffffffff;
    }
    FUN_ffff00009077f434(&local_b8,0x7f,s_pinctrl__d_ffff0000907f350d,iVar3);
    puVar4 = (uint *)FUN_ffff00009076dcd4(param_2,iVar2,&local_b8,&local_34);
    if (puVar4 == (uint *)0x0) {
      return 0xffffffff;
    }
    if (local_34 == 0) {
      return 0xffffffff;
    }
    uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
    iVar2 = FUN_ffff00009076dd7c(param_2,uVar1 >> 0x10 | uVar1 << 0x10);
    if (iVar2 < 0) {
      return 0xffffffff;
    }
    iVar3 = FUN_ffff00009076d54c(param_2,iVar2,s_pins_cmd_dat_ffff0000907fdfd8);
    if ((-1 < iVar3) ||
       (iVar3 = FUN_ffff00009076d54c(param_2,iVar2,s_pins_cmd_dat_ffff0000907e01d8), -1 < iVar3)) {
      puVar4 = (uint *)FUN_ffff00009076dcd4(param_2,iVar3,s_pinmux_ffff0000907f7018,&local_34);
      if (puVar4 == (uint *)0x0) {
        return 0xffffffff;
      }
      if (local_34 == 0) {
        return 0xffffffff;
      }
      uVar1 = (*puVar4 & 0xff00ff00) >> 8 | (*puVar4 & 0xff00ff) << 8;
      return (uVar1 >> 0x10 | uVar1 << 0x10) >> 8;
    }
  }
  return 0xffffffff;
}


