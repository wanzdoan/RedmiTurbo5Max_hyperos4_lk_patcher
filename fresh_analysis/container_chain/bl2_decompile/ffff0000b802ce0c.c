
undefined4 FUN_ffff0000b802ce0c(uint param_1)

{
  undefined4 uVar1;
  
  if ((DAT_ffff0000b812505c & 1) == 0) {
    FUN_ffff0000b802c858();
  }
  if (param_1 < 0x140) {
    uVar1 = (&DAT_ffff0000b8125060)[param_1];
  }
  else {
    FUN_ffff0000b804c33c
              (s_devinfo__s__data_index__d_out_of_ffff0000b809ba4e,s_V1_0_3_ffff0000b8093755,param_1
              );
    FUN_ffff0000b804c33c
              (s_devinfo__s__data_size__d_ffff0000b8089051,s_V1_0_3_ffff0000b8093755,0x140);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


