// ffff000090781820  size=172  FUN_ffff000090781820

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090781820(undefined8 param_1)

{
  int iVar1;
  undefined1 *local_60;
  undefined1 **ppuStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  ppuStack_58 = &local_60;
  if (_DAT_ffff00009087d054 < 0x9fff) {
    local_50 = 0;
    uStack_48 = 0xffffffc8;
    local_60 = (undefined1 *)register0x00000008;
    iVar1 = FUN_ffff00009077f488
                      ((long)_DAT_ffff00009087d054 + -0xffff6f782fa8,
                       (long)(0x9fff - _DAT_ffff00009087d054),param_1,&local_60);
    _DAT_ffff00009087d054 = _DAT_ffff00009087d054 + iVar1;
  }
  return 0;
}


