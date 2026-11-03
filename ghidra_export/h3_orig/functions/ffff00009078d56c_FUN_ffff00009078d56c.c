// ffff00009078d56c  size=2876  FUN_ffff00009078d56c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009078d56c(ulong param_1)

{
  byte bVar1;
  char cVar2;
  long lVar3;
  undefined8 uVar4;
  byte local_28 [4];
  byte local_24 [4];
  
  FUN_ffff000090785468(0x48,0);
  FUN_ffff000090785354(0x48,1);
  FUN_ffff0000907853c4(0x48,0);
  if ((param_1 & 1) != 0) {
    FUN_ffff00009077cbd8(10);
    FUN_ffff0000907853c4(0x48,1);
    FUN_ffff00009077cbd8(0xc1c);
  }
  local_28[0] = 0;
  lVar3 = FUN_ffff000090736ed0(s_mt6379_ffff0000907ea425);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s__d__Failed_to_get_regmap_ffff0000907dbd8a,
               s_eusb2_repeater_enable_ffff0000907f8dc6,0x359);
    return;
  }
  FUN_ffff00009073732c(lVar3,0,local_28,1);
  local_28[0] = local_28[0] & 0xf;
  if (local_28[0] < 3) {
    if ((param_1 & 1) == 0) {
LAB_ffff00009078df74:
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd8b,local_24,1);
      local_24[0] = local_24[0] | 0x20;
      FUN_ffff0000907372f4(lVar3,0xd8b,local_24,1);
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
      local_24[0] = local_24[0] | 1;
      FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd00,local_24,1);
      local_24[0] = local_24[0] & 0xfe;
      FUN_ffff0000907372f4(lVar3,0xd00,local_24,1);
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
      local_24[0] = local_24[0] & 0xfd;
      FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd12,local_24,1);
      local_24[0] = local_24[0] & 0xdf;
      FUN_ffff0000907372f4(lVar3,0xd12,local_24,1);
      FUN_ffff00009077cbd8(300);
      return;
    }
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd92,local_24,1);
    local_24[0] = local_24[0] | 7;
    FUN_ffff0000907372f4(lVar3,0xd92,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd8b,local_24,1);
    local_24[0] = local_24[0] | 0x20;
    FUN_ffff0000907372f4(lVar3,0xd8b,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
    local_24[0] = local_24[0] | 1;
    FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd00,local_24,1);
    local_24[0] = local_24[0] & 0xfe;
    FUN_ffff0000907372f4(lVar3,0xd00,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
    local_24[0] = local_24[0] & 0xfd;
    FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd12,local_24,1);
    local_24[0] = local_24[0] & 0xdf;
    FUN_ffff0000907372f4(lVar3,0xd12,local_24,1);
    FUN_ffff00009077cbd8(300);
    FUN_ffff00009078eb00(lVar3);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd18,local_24,1);
    local_24[0] = local_24[0] & 0xf | 0x60;
    FUN_ffff0000907372f4(lVar3,0xd18,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd19,local_24,1);
    local_24[0] = local_24[0] | 4;
    FUN_ffff0000907372f4(lVar3,0xd19,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xda5,local_24,1);
    local_24[0] = local_24[0] | 0x10;
    FUN_ffff0000907372f4(lVar3,0xda5,local_24,1);
    if (-1 < _DAT_ffff000090836724) {
      FUN_ffff00009077de54(s__EUSB2__usb20_fs_sr_0x_x_ffff0000907f05f6);
      bVar1 = DAT_ffff000090836724;
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd14,local_24,1);
      local_24[0] = local_24[0] & 0xf8 | bVar1 & 7;
      FUN_ffff0000907372f4(lVar3,0xd14,local_24,1);
    }
    if (DAT_ffff000090836728 < 0) goto LAB_ffff00009078dc3c;
    FUN_ffff00009077de54(s__EUSB2__eusb20_fsrx_hys_sel_0x_x_ffff0000907d9e5b);
    cVar2 = (char)DAT_ffff000090836728;
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd27,local_24,1);
    uVar4 = 0xd27;
    local_24[0] = local_24[0] & 0x3f | cVar2 << 6;
  }
  else {
    if ((param_1 & 1) == 0) goto LAB_ffff00009078df74;
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd92,local_24,1);
    local_24[0] = local_24[0] | 7;
    FUN_ffff0000907372f4(lVar3,0xd92,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd94,local_24,1);
    local_24[0] = local_24[0] | 2;
    FUN_ffff0000907372f4(lVar3,0xd94,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd8b,local_24,1);
    local_24[0] = local_24[0] | 0x20;
    FUN_ffff0000907372f4(lVar3,0xd8b,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
    local_24[0] = local_24[0] | 1;
    FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd00,local_24,1);
    local_24[0] = local_24[0] & 0xfe;
    FUN_ffff0000907372f4(lVar3,0xd00,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
    local_24[0] = local_24[0] & 0xfd;
    FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd12,local_24,1);
    local_24[0] = local_24[0] & 0xdf;
    FUN_ffff0000907372f4(lVar3,0xd12,local_24,1);
    FUN_ffff00009077cbd8(300);
    FUN_ffff00009078eb00(lVar3);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd18,local_24,1);
    local_24[0] = local_24[0] & 0xf | 0x60;
    FUN_ffff0000907372f4(lVar3,0xd18,local_24,1);
    if (-1 < _DAT_ffff000090836724) {
      FUN_ffff00009077de54(s__EUSB2__usb20_fs_sr_0x_x_ffff0000907f05f6);
      bVar1 = DAT_ffff000090836724;
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd14,local_24,1);
      local_24[0] = local_24[0] & 0xf8 | bVar1 & 7;
      FUN_ffff0000907372f4(lVar3,0xd14,local_24,1);
    }
    if (-1 < DAT_ffff000090836728) {
      FUN_ffff00009077de54(s__EUSB2__eusb20_fsrx_hys_sel_0x_x_ffff0000907d9e5b);
      cVar2 = (char)DAT_ffff000090836728;
      local_24[0] = 0;
      FUN_ffff00009073732c(lVar3,0xd27,local_24,1);
      local_24[0] = local_24[0] & 0x3f | cVar2 << 6;
      FUN_ffff0000907372f4(lVar3,0xd27,local_24,1);
    }
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd20,local_24,1);
    local_24[0] = local_24[0] | 4;
    FUN_ffff0000907372f4(lVar3,0xd20,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd8a,local_24,1);
    local_24[0] = local_24[0] | 1;
    FUN_ffff0000907372f4(lVar3,0xd8a,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd8a,local_24,1);
    local_24[0] = local_24[0] | 2;
    FUN_ffff0000907372f4(lVar3,0xd8a,local_24,1);
    local_24[0] = 0;
    FUN_ffff00009073732c(lVar3,0xd8a,local_24,1);
    uVar4 = 0xd8a;
    local_24[0] = local_24[0] & 0xfb;
  }
  FUN_ffff0000907372f4(lVar3,uVar4,local_24,1);
LAB_ffff00009078dc3c:
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd35,local_24,1);
  local_24[0] = local_24[0] | 6;
  FUN_ffff0000907372f4(lVar3,0xd35,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd24,local_24,1);
  local_24[0] = local_24[0] | 0x10;
  FUN_ffff0000907372f4(lVar3,0xd24,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd31,local_24,1);
  local_24[0] = 0x52;
  FUN_ffff0000907372f4(lVar3,0xd31,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd12,local_24,1);
  local_24[0] = local_24[0] | 0x20;
  FUN_ffff0000907372f4(lVar3,0xd12,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd89,local_24,1);
  local_24[0] = local_24[0] | 0x80;
  FUN_ffff0000907372f4(lVar3,0xd89,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd8b,local_24,1);
  local_24[0] = local_24[0] & 0xdf;
  FUN_ffff0000907372f4(lVar3,0xd8b,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd00,local_24,1);
  local_24[0] = local_24[0] | 1;
  FUN_ffff0000907372f4(lVar3,0xd00,local_24,1);
  FUN_ffff00009077cbd8(100);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
  local_24[0] = local_24[0] & 0xfe;
  FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
  FUN_ffff00009077cbd8(0x32);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd34,local_24,1);
  local_24[0] = local_24[0] | 1;
  FUN_ffff0000907372f4(lVar3,0xd34,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
  local_24[0] = local_24[0] | 2;
  FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
  FUN_ffff00009077cbd8(10);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
  local_24[0] = local_24[0] & 0xfd;
  FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
  FUN_ffff00009077cbd8(0x1e);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd34,local_24,1);
  local_24[0] = local_24[0] & 0xfe;
  FUN_ffff0000907372f4(lVar3,0xd34,local_24,1);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd03,local_24,1);
  local_24[0] = local_24[0] | 2;
  FUN_ffff0000907372f4(lVar3,0xd03,local_24,1);
  FUN_ffff00009077cbd8(100);
  local_24[0] = 0;
  FUN_ffff00009073732c(lVar3,0xd89,local_24,1);
  local_24[0] = local_24[0] & 0x7f;
  FUN_ffff0000907372f4(lVar3,0xd89,local_24,1);
  return;
}


