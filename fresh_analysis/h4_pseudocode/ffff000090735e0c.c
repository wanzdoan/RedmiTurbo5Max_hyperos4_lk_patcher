
uint FUN_ffff000090735e0c(long param_1,long param_2)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff00009077e11c(s_can_pass_NULL_header_0x_p__and_e_ffff0000907f64a4,param_1,param_2);
    uVar3 = 0xffffffea;
  }
  else {
    lVar2 = FUN_ffff00009077bf18(s_bootdevice_ffff0000907d0d09);
    if (lVar2 == 0) {
      uVar3 = 0xffffffed;
    }
    else {
      FUN_ffff000090717738(&LAB_ffff0000908192f0,0xffffffff);
      FUN_ffff000090734a8c(lVar2,param_1,param_2);
      FUN_ffff000090734e10(lVar2,param_1,param_2);
      FUN_ffff0000907809c4(s_bootdevice_ffff0000907d0d09,0);
      FUN_ffff0000907177d0(&LAB_ffff0000908192f0);
      iVar1 = FUN_ffff000090735980();
      uVar3 = iVar1 >> 0x1f & 0xfffffffb;
      FUN_ffff00009077bfac(lVar2);
    }
  }
  return uVar3;
}


