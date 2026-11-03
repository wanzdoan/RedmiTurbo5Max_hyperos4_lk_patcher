
void FUN_ffff0000b801b5b0(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff0000b801b570();
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c(s__KVM_INIT__kvm_compat_pre_proces_ffff0000b808cfe3,iVar1);
  }
  iVar1 = FUN_ffff0000b801b630(param_1);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c(s__KVM_INIT__pkvm_mkp_init__failed_ffff0000b808de11,iVar1);
  }
  iVar1 = FUN_ffff0000b801b5a0(param_1);
  if (iVar1 != 0) {
    FUN_ffff0000b804c33c(s__KVM_INIT__kvm_compat_post_proce_ffff0000b8092568,iVar1);
    return;
  }
  return;
}


