
long FUN_ffff0000b802d110(undefined8 param_1,long *param_2)

{
  int iVar1;
  long *plVar2;
  long local_18;
  
  plVar2 = (long *)FUN_ffff0000b800db20(param_1,0);
  if (plVar2 == (long *)0x0) {
    FUN_ffff0000b804c33c
              (s__s_query__s_mblock_failed_ffff0000b809d966,s__DICE__ffff0000b8099de7,param_1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x1b,&DAT_ffff0000b8086efb);
  }
  local_18 = *plVar2 + -0x1000000000000;
  iVar1 = FUN_ffff0000b804a5f8(PTR_DAT_ffff0000b81086d8,param_1,plVar2[1],&local_18,0xc,*plVar2,0,0)
  ;
  if (iVar1 == 0) {
    *param_2 = *plVar2;
    return local_18;
  }
  FUN_ffff0000b804c33c
            (s__s_vmm_alloc_physical_failed__er_ffff0000b808f00f,s__DICE__ffff0000b8099de7,iVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_dice_di_ffff0000b8099dee,0x2a,&DAT_ffff0000b8086efb);
}


