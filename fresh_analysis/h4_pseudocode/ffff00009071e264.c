
uint FUN_ffff00009071e264(undefined8 param_1,undefined8 param_2)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined4 local_24;
  
  local_24 = 0;
  iVar2 = FUN_ffff00009076da24(param_1,s__apusys_rv_4c200000_ffff00009080229b);
  if ((((iVar2 < 0) &&
       (iVar2 = FUN_ffff00009076da24(param_1,s__soc_apusys_rv_4c200000_ffff0000907ea988), iVar2 < 0)
       ) && (iVar2 = FUN_ffff00009076da24(param_1,s__apusys_rv_ffff0000907edd87), iVar2 < 0)) &&
     (iVar2 = FUN_ffff00009076da24(param_1,s__soc_apusys_rv_ffff0000907f9694), iVar2 < 0)) {
    FUN_ffff00009077e11c
              (s__s__apusys_rv_not_found_ffff0000907da7a1,
               s_get_apusys_rv_fdt_path_offset_ffff0000907f7e13);
    return 0xfffffffe;
  }
  lVar3 = FUN_ffff00009076ddb4(param_1,iVar2,param_2,&local_24);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c
              (s__s___s_not_found_ffff0000907da7ba,s_apusys_rv_get_buf_sz_ffff0000907f96a3,param_2);
    return 0xfffffffe;
  }
  uVar1 = (*(uint *)(lVar3 + 0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 0xc) & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


