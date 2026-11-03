
undefined8 FUN_ffff000090788bf8(undefined4 param_1)

{
  int iVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  uVar2 = FUN_ffff00009071fb70();
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077e11c(s__SEC__Update_version__boot_succe_ffff0000907d3940);
  }
  else {
    FUN_ffff00009071f790();
    uVar2 = FUN_ffff00009071f700();
    iVar1 = FUN_ffff00009071fa24(uVar2,1);
    if (iVar1 == 0) {
      FUN_ffff00009077e11c(s__SEC__Bypass_version_update_due_t_ffff0000907f561a,uVar2 & 0xffffffff);
      return 0;
    }
    FUN_ffff00009077e11c(s__SEC__Update_version__boot_succe_ffff0000907d0317,uVar2 & 0xffffffff);
  }
  uVar3 = FUN_ffff0000907ac66c(param_1,0);
  return uVar3;
}


