
/* android::base::SetProperty(std::string const&, std::string const&) */

bool android::base::SetProperty(string *param_1,string *param_2)

{
  int iVar1;
  string *psVar2;
  string *psVar3;

  psVar2 = *(string **)(param_1 + 0x10);
  psVar3 = *(string **)(param_2 + 0x10);
  if (((byte)*param_1 & 1) == 0) {
    psVar2 = param_1 + 1;
  }
  if (((byte)*param_2 & 1) == 0) {
    psVar3 = param_2 + 1;
  }
  iVar1 = __system_property_set(psVar2,psVar3);
  return iVar1 == 0;
}
