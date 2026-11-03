// ffff0000907340f0  size=300  FUN_ffff0000907340f0

void FUN_ffff0000907340f0(undefined8 param_1)

{
  int iVar1;
  ulong uVar2;
  ulong local_50;
  long lStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  ulong local_18;
  
  iVar1 = FUN_ffff00009071c57c();
  if (iVar1 == 0) {
    uVar2 = FUN_ffff000090719798
                      (0x180000,0x10000,0x100000000,0,0,s_me_GPUEB_SHARED_ffff0000907e487b);
  }
  else {
    uVar2 = FUN_ffff000090719f58
                      (0x180000,0x10000,0x240000000,0x340000000,0,0,
                       s_me_GPUEB_SHARED_ffff0000907e487b);
  }
  if (uVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__MFGSYS_SHARED_mblock_allocat_ffff0000907fb6c3,s__MFGSYS__LDR__ffff0000907ef9a9)
    ;
  }
  lStack_48 = uVar2 + 0x180000;
  uStack_38 = 0;
  local_40 = 0x1f;
  local_50 = uVar2;
  FUN_ffff00009078ae3c(&local_50);
  iVar1 = FUN_ffff00009076d75c(param_1,s__soc_gpueb_ffff0000907e770e);
  if (-1 < iVar1) {
    uVar2 = (uVar2 & 0xff00ff00ff00ff00) >> 8 | (uVar2 & 0xff00ff00ff00ff) << 8;
    uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
    local_18 = uVar2 >> 0x20 | uVar2 << 0x20;
    FUN_ffff00009076e40c(param_1,iVar1,s_gpueb_mem_addr_ffff0000908009c5,&local_18,8);
    local_18 = 0x180000000000;
    FUN_ffff00009076e40c(param_1,iVar1,s_gpueb_mem_size_ffff0000907e7929,&local_18,8);
  }
  return;
}


