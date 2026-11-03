// ffff00009074af94  size=128  FUN_ffff00009074af94

undefined4 FUN_ffff00009074af94(uint param_1)

{
  undefined4 uVar1;
  
  if ((DAT_ffff000090877680 & 1) == 0) {
    FUN_ffff00009074a9e0();
  }
  if (param_1 < 0x140) {
    uVar1 = *(undefined4 *)((ulong)param_1 * 4 + -0xffff6f78897c);
  }
  else {
    FUN_ffff00009077de54
              (s_devinfo__s__data_index__d_out_of_ffff0000907f8594,s_V1_0_3_ffff0000907e970c,param_1
              );
    FUN_ffff00009077de54
              (s_devinfo__s__data_size__d_ffff0000907d64ce,s_V1_0_3_ffff0000907e970c,0x140);
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


