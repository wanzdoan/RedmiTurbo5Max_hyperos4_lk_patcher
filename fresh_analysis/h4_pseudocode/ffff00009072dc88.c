
long FUN_ffff00009072dc88(undefined8 param_1,uint param_2,undefined8 *param_3)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  undefined8 local_60;
  undefined4 local_54;
  undefined8 local_50;
  undefined8 local_48;
  undefined8 local_28;
  
  local_28 = 0;
  local_48 = 0;
  local_50 = 0;
  local_54 = 0;
  local_60 = 0;
  iVar1 = FUN_ffff00009072e2bc(param_1,&local_48,&local_28,&local_50,&local_54);
  if (iVar1 < 0) {
    FUN_ffff000090730228
              (s__CCCI___s__d__get_limit_and_alig_ffff000090802506,
               s_ccci_resv_named_memory_ffff000090800a64,0x28,param_1);
    lVar2 = 0;
  }
  else {
    lVar2 = FUN_ffff00009071a09c(param_2,local_50,local_48,local_28,0,local_54,param_1);
    if (lVar2 == 0) {
      FUN_ffff000090730228
                (s__CCCI___s__d__alloc_sz_0x_x__ali_ffff0000907fd275,
                 s_ccci_resv_named_memory_ffff000090800a64,0x2f,param_2,local_50,local_48,local_28,
                 param_1);
    }
    else {
      if (param_3 != (undefined8 *)0x0) {
        uVar3 = 0x15;
        if ((param_2 & 0x1fffff) != 0) {
          uVar3 = 0xc;
        }
        iVar1 = FUN_ffff00009077ba64
                          (PTR_DAT_ffff000090839928,param_1,param_2,&local_60,uVar3,lVar2,0,0);
        if (iVar1 != 0) {
          FUN_ffff000090730228
                    (s__CCCI___s__d__map_va_for_pa_fail_ffff0000907ef954,
                     s_ccci_resv_named_memory_ffff000090800a64,0x3c,iVar1);
          FUN_ffff00009071ae70(lVar2,param_2);
          return 0;
        }
        *param_3 = local_60;
      }
      FUN_ffff000090730228
                (s__CCCI___s__d__mblock_alloc_sz_0x_ffff0000907ec3e5,
                 s_ccci_resv_named_memory_ffff000090800a64,0x46,param_2,local_50,local_48,local_28);
      FUN_ffff000090730228
                (s__CCCI___s__d__mblock_alloc_resul_ffff0000907cf5ea,
                 s_ccci_resv_named_memory_ffff000090800a64,0x47,param_1,lVar2,local_60);
    }
  }
  return lVar2;
}


