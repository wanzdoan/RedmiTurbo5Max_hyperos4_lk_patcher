// ffff00009071e0d8  size=228  FUN_ffff00009071e0d8

uint FUN_ffff00009071e0d8(undefined8 param_1,undefined8 param_2)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  undefined4 local_24;
  
  local_24 = 0;
  iVar2 = FUN_ffff00009076d75c(param_1,s__apusys_rv_4c200000_ffff0000908020b5);
  if ((((iVar2 < 0) &&
       (iVar2 = FUN_ffff00009076d75c(param_1,s__soc_apusys_rv_4c200000_ffff0000907ea7ee), iVar2 < 0)
       ) && (iVar2 = FUN_ffff00009076d75c(param_1,s__apusys_rv_ffff0000907edbff), iVar2 < 0)) &&
     (iVar2 = FUN_ffff00009076d75c(param_1,s__soc_apusys_rv_ffff0000907f94f8), iVar2 < 0)) {
    FUN_ffff00009077de54
              (s__s__apusys_rv_not_found_ffff0000907da67f,
               s_get_apusys_rv_fdt_path_offset_ffff0000907f7c77);
    return 0xfffffffe;
  }
  lVar3 = FUN_ffff00009076daec(param_1,iVar2,param_2,&local_24);
  if (lVar3 == 0) {
    FUN_ffff00009077de54
              (s__s___s_not_found_ffff0000907da698,s_apusys_rv_get_buf_sz_ffff0000907f9507,param_2);
    return 0xfffffffe;
  }
  uVar1 = (*(uint *)(lVar3 + 0xc) & 0xff00ff00) >> 8 | (*(uint *)(lVar3 + 0xc) & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


