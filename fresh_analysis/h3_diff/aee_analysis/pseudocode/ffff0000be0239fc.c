
void FUN_ffff0000be0239fc(void)

{
  long lVar1;
  undefined *puVar2;
  uint uVar3;
  undefined8 local_48;
  long lStack_40;
  undefined8 local_38;
  
  FUN_ffff0000be05fd98
            (s_leds__s__set_default_level_ffff0000be0b8913,PTR_s_lcd_backlight_ffff0000be0d6518);
  puVar2 = PTR_s_lcd_backlight_ffff0000be0d6518;
  local_48 = DAT_ffff0000be117a80;
  lStack_40 = DAT_ffff0000be117a88;
  local_38 = DAT_ffff0000be117a90;
  uVar3 = FUN_ffff0000be024008(PTR_s_lcd_backlight_ffff0000be0d6518,&local_48);
  if (7 < uVar3) {
    FUN_ffff0000be05fd98(s_get_the_LED_index_by_name_failed_ffff0000be0b2e33,puVar2);
    return;
  }
  lVar1 = (ulong)uVar3 * 0x20;
  FUN_ffff0000be05fd98
            (s_get_leds_index___d__s_set__d_ffff0000be0c1a18,(ulong)uVar3,puVar2,
             *(undefined4 *)(DAT_ffff0000be117a88 + lVar1 + 0xc));
  FUN_ffff0000be02386c(puVar2,*(undefined4 *)(DAT_ffff0000be117a88 + lVar1 + 0xc));
  return;
}


