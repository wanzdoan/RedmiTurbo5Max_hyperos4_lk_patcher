
undefined8 ccci_map_phy_addr(ulong param_1,undefined4 param_2)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  char *pcVar4;
  
  uVar3 = param_1 & 0xfffffffffffff000;
  iVar1 = pfn_is_map_memory(param_1 >> 0xc);
  if (iVar1 == 0) {
    uVar2 = 0x6800000000070b;
    if (arm64_use_ng_mappings != '\0') {
      uVar2 = 0x68000000000f0b;
    }
    uVar2 = ioremap_prot(uVar3,param_2,uVar2);
    pcVar4 = "[0/util]ioremap_wc: (%lx %llx %d)\n";
  }
  else {
    uVar2 = 0x6800000000070b;
    if (arm64_use_ng_mappings != '\0') {
      uVar2 = 0x68000000000f0b;
    }
    uVar2 = vmap_reserved_mem(uVar3,param_2,uVar2);
    pcVar4 = "[0/util]vmap_reserved_mem: (%lx %llx %d)\n";
  }
  ccci_dump_write(0,1,pcVar4,uVar3,uVar2,param_2);
  ccci_dump_write(2,1,pcVar4,uVar3,uVar2,param_2);
  return uVar2;
}

