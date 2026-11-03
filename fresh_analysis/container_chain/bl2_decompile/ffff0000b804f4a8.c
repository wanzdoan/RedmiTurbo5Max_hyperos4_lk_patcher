
undefined8 FUN_ffff0000b804f4a8(undefined8 param_1)

{
  int iVar1;
  undefined1 *local_60;
  undefined1 **ppuStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  ppuStack_58 = &local_60;
  if (DAT_ffff0000b8129c5c < 0x9fff) {
    local_50 = 0;
    uStack_48 = 0xffffffc8;
    local_60 = (undefined1 *)register0x00000008;
    iVar1 = FUN_ffff0000b804d7a0
                      ((long)DAT_ffff0000b8129c5c + -0xffff47ed63a0,
                       (long)(0x9fff - DAT_ffff0000b8129c5c),param_1,&local_60);
    DAT_ffff0000b8129c5c = DAT_ffff0000b8129c5c + iVar1;
  }
  return 0;
}


