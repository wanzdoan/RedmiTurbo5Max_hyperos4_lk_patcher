// ffff00009071ec08  size=120  FUN_ffff00009071ec08

undefined8
FUN_ffff00009071ec08
          (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
          undefined8 param_5)

{
  ulong uVar1;
  undefined4 uVar2;
  int iVar3;
  long lVar4;
  undefined4 uStack_44;
  
  uVar1 = daif;
  daif = uVar1 | 0x80;
  FUN_ffff0000907602ac();
  FUN_ffff000090703a64();
  lVar4 = FUN_ffff00009077b334(param_1);
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cee0(s_error_translating_entry_physical_ffff0000907ec1af);
  }
  FUN_ffff00009071eb64(3);
  FUN_ffff000090707fa0();
  FUN_ffff00009071e998(lVar4,param_2,param_3,param_4,param_5);
  uStack_44 = 0;
  iVar3 = FUN_ffff0000907375fc(&uStack_44);
  uVar2 = uStack_44;
  if (iVar3 == 0) {
    iVar3 = FUN_ffff0000907375fc(&uStack_44);
    if (iVar3 == 0) {
      return CONCAT44(uVar2,uStack_44);
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_arch_ar_ffff0000907f7e9c,0x1c,
               s__get_rnd__rnd_val__ffff0000907dc47d);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_arch_ar_ffff0000907f7e9c,0x1a,
             s__get_rnd__rnd_val__ffff0000907dc47d);
}


