
undefined4 FUN_ffff00009074b22c(uint param_1)

{
  undefined4 uVar1;
  
  if ((DAT_ffff000090877718 & 1) == 0) {
    FUN_ffff00009074ac78();
  }
  if (param_1 < 0x140) {
    uVar1 = (&DAT_ffff00009087771c)[param_1];
  }
  else {
    FUN_ffff00009077e11c
              (s_devinfo__s__data_index__d_out_of_ffff0000907f8730,s_V1_0_3_ffff0000907e9850,param_1
              );
    FUN_ffff00009077e11c
              (s_devinfo__s__data_size__d_ffff0000907d6564,s_V1_0_3_ffff0000907e9850,0x140);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


