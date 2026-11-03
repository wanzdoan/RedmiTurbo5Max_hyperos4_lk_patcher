
undefined8 FUN_ffff0000be00a12c(void)

{
  byte bVar1;
  int iVar2;
  undefined4 uVar3;
  char *pcVar4;
  ulong uVar5;
  undefined8 uVar6;
  
  iVar2 = FUN_ffff0000be065774();
  if (iVar2 == 0xfc11) {
    DAT_ffff0000be0f90d8 = FUN_ffff0000be0651f8();
    FUN_ffff0000be0114f0(s_output_device____d_ffff0000be0a309b,DAT_ffff0000be0f90d8);
    if (DAT_ffff0000be0f90d8 != 0) {
      DAT_ffff0000be0f90d0 = FUN_ffff0000be011c48();
      if (DAT_ffff0000be0f90d0 == 0) {
        FUN_ffff0000be05fd98(s_MT_RAMDUMP_control_block_not_fou_ffff0000be0a8c77);
        return 0xffffffff;
      }
      bVar1 = *(byte *)(DAT_ffff0000be0f90d0 + 0xf8);
      iVar2 = FUN_ffff0000be0640b4();
      if (iVar2 == 0) {
        pcVar4 = s__s__d__DDR_reserve_mode_disabled_ffff0000be0b973f;
        uVar6 = 0x11c;
      }
      else {
        iVar2 = FUN_ffff0000be0640c8();
        if (iVar2 != 0) {
          FUN_ffff0000be0114f0(s_sram_record_with_mode__d_ffff0000be0c5f24,bVar1);
          if (8 < bVar1 - 1) {
            if (bVar1 != 0) {
              FUN_ffff0000be0115fc(s_Unsupport_exception_type_ffff0000be0a55c9);
              return 0xffffffff;
            }
            uVar5 = FUN_ffff0000be063c54();
            if ((uVar5 & 1) == 0) {
              return 0xffffffff;
            }
            *(undefined4 *)(DAT_ffff0000be0f90d0 + 0xf8) = 6;
            FUN_ffff0000be0114f0(s_set_reboot_mode_to_MRDUMP_KEY_ffff0000be0ae321);
          }
          if (DAT_ffff0000be0f90d8 != 5) {
            return 1;
          }
          FUN_ffff0000be013f30();
          return 0;
        }
        pcVar4 = s__s__d__DDR_reserve_mode_failed_ffff0000be0c032f;
        uVar6 = 0x121;
      }
      FUN_ffff0000be05fd98(pcVar4,s_mrdump_detection_ffff0000be0a9f46,uVar6);
      return 0xffffffff;
    }
  }
  else {
    uVar3 = FUN_ffff0000be065774();
    FUN_ffff0000be0113f4(s_mrdump_disabled__d_ffff0000be0c17fd,uVar3);
  }
  return 0xffffffff;
}


