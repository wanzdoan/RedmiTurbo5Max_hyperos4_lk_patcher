
undefined8 FUN_ffff000090781ae8(undefined8 param_1)

{
  int iVar1;
  undefined1 *local_60;
  undefined1 **ppuStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  ppuStack_58 = &local_60;
  if (DAT_ffff00009087d0ec < 0x9fff) {
    local_50 = 0;
    uStack_48 = 0xffffffc8;
    local_60 = (undefined1 *)register0x00000008;
    iVar1 = FUN_ffff00009077f750
                      ((long)DAT_ffff00009087d0ec + -0xffff6f782f10,
                       (long)(0x9fff - DAT_ffff00009087d0ec),param_1,&local_60);
    DAT_ffff00009087d0ec = DAT_ffff00009087d0ec + iVar1;
  }
  return 0;
}


