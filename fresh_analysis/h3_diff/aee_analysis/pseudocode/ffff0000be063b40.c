
undefined8 FUN_ffff0000be063b40(undefined8 param_1)

{
  int iVar1;
  undefined1 *local_60;
  undefined1 **ppuStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  ppuStack_58 = &local_60;
  if (DAT_ffff0000be1281b8 < 0x9fff) {
    local_50 = 0;
    uStack_48 = 0xffffffc8;
    local_60 = (undefined1 *)register0x00000008;
    iVar1 = FUN_ffff0000be061230
                      (&DAT_ffff0000be1281bc + DAT_ffff0000be1281b8,
                       (long)(0x9fff - DAT_ffff0000be1281b8),param_1,&local_60);
    DAT_ffff0000be1281b8 = DAT_ffff0000be1281b8 + iVar1;
  }
  return 0;
}


