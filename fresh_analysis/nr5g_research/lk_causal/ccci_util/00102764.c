
void get_md_postfix(long param_1,long param_2,long param_3)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  ulong local_48;
  undefined8 uStack_40;
  long local_38;
  
  lVar1 = sp_el0;
  local_38 = *(long *)(lVar1 + 0x620);
  local_48 = 0;
  uStack_40 = 0;
  iVar2 = get_md_img_type();
  if (iVar2 == 0) {
    local_48 = local_48 & 0xffffffffffffff00;
    uVar3 = ccci_get_md_cap_str(3);
    if (param_1 == 0) {
      scnprintf(&local_48,0x10,"_%s_n",uVar3);
    }
    else {
      scnprintf(&local_48,0x10,"_%s_%s",uVar3,param_1);
    }
    if (param_2 != 0) {
      scnprintf(param_2,0x10,&DAT_001103ff,&local_48);
      ccci_dump_write(0,1,"[0/util]MD image postfix=%s\n",param_2);
      ccci_dump_write(2,1,"[0/util]MD image postfix=%s\n",param_2);
    }
    if (param_3 != 0) {
      scnprintf(param_3,0x10,"%s_E%d",&local_48,1);
      ccci_dump_write(0,1,"[0/util]MD image postfix=%s\n",param_3);
      ccci_dump_write(2,1,"[0/util]MD image postfix=%s\n",param_3);
    }
  }
  else {
    if (param_2 != 0) {
      uVar3 = ccci_get_md_cap_str(iVar2);
      scnprintf(param_2,0x10,&DAT_0011152a,uVar3);
      ccci_dump_write(0,1,"[0/util]MD image postfix=%s\n",param_2);
      _printk(&DAT_0010ec8b,param_2);
    }
    if (param_3 != 0) {
      uVar3 = ccci_get_md_cap_str(iVar2);
      scnprintf(param_3,0x10,"%s_n_E%d",uVar3,0);
      ccci_dump_write(0,1,"[0/util]MD image postfix=%s\n",param_3);
      _printk(&DAT_0010ec8b,param_3);
    }
  }
  lVar1 = sp_el0;
  if (*(long *)(lVar1 + 0x620) != local_38) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}

