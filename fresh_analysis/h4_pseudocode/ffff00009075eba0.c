
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075eba0(code *param_1)

{
  ulong uVar1;
  
  _DAT_ffff00000011b200 = _DAT_ffff00000c1f0078 & 0x3ff0000;
  _DAT_ffff00000011b204 = _DAT_ffff00000c1f0018 & 0xffffff;
  _DAT_ffff00000011b208 = _DAT_ffff00000c320014 & 0x3ff;
  _DAT_ffff00000011b20c = _DAT_ffff00000c330014 & 0x3ff;
  _DAT_ffff00000011b210 = _DAT_ffff00000c340014 & 0x3ff;
  _DAT_ffff00000011b214 = _DAT_ffff00000c350014 & 0x3ff;
  _DAT_ffff00000011b218 = _DAT_ffff00000c360014 & 0x3ff;
  _DAT_ffff00000011b21c = _DAT_ffff00000c370014 & 0x3ff;
  _DAT_ffff00000011b220 = _DAT_ffff00000c380014 & 0x3ff;
  _DAT_ffff00000011b224 = _DAT_ffff00000c390014 & 0x3ff;
  _DAT_ffff00000011b228 = _DAT_ffff00000c1f02a0;
  _DAT_ffff00000011b22c = _DAT_ffff00000c1f02a4;
  _DAT_ffff00000011b230 = _DAT_ffff00000c1f02a8;
  _DAT_ffff00000011b234 = _DAT_ffff00000c1f02ac;
  _DAT_ffff00000011b238 = _DAT_ffff00000c1f02b0;
  uVar1 = (*param_1)(0x11b000,0x800);
  if ((uVar1 & 1) != 0) {
    return;
  }
  FUN_ffff00009077e11c
            (s__s__mcdi_aee_dump_failed_ffff0000907fdeb7,s_save_mcdi_data_ffff0000907d4d61);
  return;
}


