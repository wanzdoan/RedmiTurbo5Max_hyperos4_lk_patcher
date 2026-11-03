
void FUN_ffff0000b801b994(void)

{
  long lVar1;
  undefined *puVar2;
  uint uVar3;
  undefined8 local_48;
  long lStack_40;
  undefined8 local_38;
  
  FUN_ffff0000b804c33c
            (s_leds__s__set_default_level_ffff0000b8096355,PTR_s_lcd_backlight_ffff0000b80e8fb8);
  puVar2 = PTR_s_lcd_backlight_ffff0000b80e8fb8;
  local_48 = DAT_ffff0000b811b268;
  lStack_40 = DAT_ffff0000b811b270;
  local_38 = DAT_ffff0000b811b278;
  uVar3 = FUN_ffff0000b801bfdc(PTR_s_lcd_backlight_ffff0000b80e8fb8,&local_48);
  if (7 < uVar3) {
    FUN_ffff0000b804c33c(s_get_the_LED_index_by_name_failed_ffff0000b80917e0,puVar2);
    return;
  }
  lVar1 = (ulong)uVar3 * 0x20;
  FUN_ffff0000b804c33c
            (s_get_leds_index___d__s_set__d_ffff0000b809d609,(ulong)uVar3,puVar2,
             *(undefined4 *)(DAT_ffff0000b811b270 + lVar1 + 0xc));
  FUN_ffff0000b801b7ac(puVar2,*(undefined4 *)(DAT_ffff0000b811b270 + lVar1 + 0xc));
  return;
}


