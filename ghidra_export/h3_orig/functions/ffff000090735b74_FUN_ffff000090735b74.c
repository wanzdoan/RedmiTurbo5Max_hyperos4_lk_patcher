// ffff000090735b74  size=212  FUN_ffff000090735b74

uint FUN_ffff000090735b74(long param_1,long param_2)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  
  if ((param_1 == 0) || (param_2 == 0)) {
    FUN_ffff00009077de54(s_can_pass_NULL_header_0x_p__and_e_ffff0000907f631a,param_1,param_2);
    uVar3 = 0xffffffea;
  }
  else {
    lVar2 = FUN_ffff00009077bc50(s_bootdevice_ffff0000907d0ccc);
    if (lVar2 == 0) {
      uVar3 = 0xffffffed;
    }
    else {
      FUN_ffff0000907175f4(&LAB_ffff0000908192d0,0xffffffff);
      FUN_ffff0000907347f4(lVar2,param_1,param_2);
      FUN_ffff000090734b78(lVar2,param_1,param_2);
      FUN_ffff0000907806fc(s_bootdevice_ffff0000907d0ccc,0);
      FUN_ffff00009071768c(&LAB_ffff0000908192d0);
      iVar1 = FUN_ffff0000907356e8();
      uVar3 = iVar1 >> 0x1f & 0xfffffffb;
      FUN_ffff00009077bce4(lVar2);
    }
  }
  return uVar3;
}


