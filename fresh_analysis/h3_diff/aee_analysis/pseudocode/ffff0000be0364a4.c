
undefined4 FUN_ffff0000be0364a4(uint param_1)

{
  undefined4 uVar1;
  
  if ((DAT_ffff0000be12185c & 1) == 0) {
    FUN_ffff0000be035ef0();
  }
  if (param_1 < 0x140) {
    uVar1 = (&DAT_ffff0000be121860)[param_1];
  }
  else {
    FUN_ffff0000be05fd98
              (s_devinfo__s__data_index__d_out_of_ffff0000be0bf9de,s_V1_0_3_ffff0000be0b5528,param_1
              );
    FUN_ffff0000be05fd98
              (s_devinfo__s__data_size__d_ffff0000be0a816c,s_V1_0_3_ffff0000be0b5528,0x140);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


