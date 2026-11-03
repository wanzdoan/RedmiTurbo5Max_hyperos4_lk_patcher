// ffff000090712d74  size=272  FUN_ffff000090712d74

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090712d74(char param_1)

{
  char *pcVar1;
  uint uVar2;
  uint uVar3;
  
  uVar3 = _DAT_ffff00009083f804 >> 2 & 1;
  uVar2 = _DAT_ffff00009083f804 & 1 ^ 1;
  if (param_1 == '\x04') {
    pcVar1 = s_androidboot_usbconfig_0_usbconfi_ffff0000907dbfc5;
    if ((_DAT_ffff00009083f804 & 1) != 0) {
      pcVar1 = s_androidboot_usbconfig_1_usbconfi_ffff0000907e8cb1;
    }
    FUN_ffff000090718f34(pcVar1);
    FUN_ffff00009077de54
              (s__s__d__adb__d_elt__d_ffff0000907d3f5a,s_set_usbconfig_ffff0000907dbfe9,0x53,uVar2,
               uVar3);
  }
  else if (param_1 == '\x01') {
    FUN_ffff000090718f34
              (&DAT_ffff000090804290 +
               *(int *)(&DAT_ffff000090804290 + ((ulong)_DAT_ffff00009083f804 & 5) * 4));
    pcVar1 = s_androidboot_mblogenable_1_ffff0000907e420f;
    if ((_DAT_ffff00009083f804 & 2) != 0) {
      pcVar1 = s_androidboot_mblogenable_0_ffff0000907d0b44;
    }
    FUN_ffff000090718f34(pcVar1);
    FUN_ffff00009077de54
              (s__s__d__adb__d_elt__d_meta_com_id_ffff0000907d3f35,s_set_usbconfig_ffff0000907dbfe9,
               0x4c,uVar2,uVar3,_DAT_ffff00009083f804);
  }
  pcVar1 = s_androidboot_meta_log_disable_1_ffff0000907dd931;
  if (_DAT_ffff00009083f80c != 1) {
    pcVar1 = s_androidboot_meta_log_disable_0_ffff0000907fb030;
  }
  FUN_ffff000090718f34(pcVar1);
  return;
}


