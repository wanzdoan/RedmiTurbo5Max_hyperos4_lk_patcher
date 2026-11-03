// ffff000090788938  size=144  FUN_ffff000090788938

undefined8 FUN_ffff000090788938(undefined4 param_1)

{
  int iVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  uVar2 = FUN_ffff00009071f9e8();
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077de54(s__SEC__Update_version__boot_succe_ffff0000907d3903);
  }
  else {
    FUN_ffff00009071f608();
    uVar2 = FUN_ffff00009071f578();
    iVar1 = FUN_ffff00009071f89c(uVar2,1);
    if (iVar1 == 0) {
      FUN_ffff00009077de54(s__SEC__Bypass_version_update_due_t_ffff0000907f54ad,uVar2 & 0xffffffff);
      return 0;
    }
    FUN_ffff00009077de54(s__SEC__Update_version__boot_succe_ffff0000907d02f8,uVar2 & 0xffffffff);
  }
  uVar3 = FUN_ffff0000907ac3ac(param_1,0);
  return uVar3;
}


